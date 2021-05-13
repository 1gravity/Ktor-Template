package com.onegravity.adapter

import com.onegravity.util.ValidationException
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.util.pipeline.*
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source

// we can suppress BlockingMethodInNonBlockingContext, it's a compiler not a code problem
@Suppress("BlockingMethodInNonBlockingContext")
class MoshiContentConverter(private val moshi: Moshi = Moshi.Builder().build()) : ContentConverter {

    override suspend fun convertForReceive(context: PipelineContext<ApplicationReceiveRequest, ApplicationCall>): Any? {
        val result = runCatching {
            val request = context.subject
            val channel = request.value as? ByteReadChannel ?: return null
            return withContext(Dispatchers.IO) {
                val source = channel.toInputStream().source().buffer()
                val type = request.type
                moshi.adapter(type.javaObjectType).fromJson(source)
            }
        }
        return if (result.isFailure) {
            when (val ex = result.exceptionOrNull()) {
                is JsonDataException -> throw ValidationException("Incorrect json format: ${ex.message}")
                is JsonEncodingException -> throw ValidationException("Invalid json format: ${ex.message}")
                else -> throw ValidationException(ex?.message ?: "")
            }
        } else {
            result.getOrNull()
        }
    }

    override suspend fun convertForSend(context: PipelineContext<Any, ApplicationCall>, contentType: ContentType, value: Any): Any {
        return TextContent(moshi.adapter(value.javaClass).toJson(value), contentType.withCharset(context.call.suitableCharset()))
    }
}

/**
 * Creates a new Moshi instance and registers it as a content converter for
 * `application/json` data.  The supplied block is used to configure the builder.
 */
fun ContentNegotiation.Configuration.moshi(block: Moshi.Builder.() -> Unit) {
    val builder = Moshi.Builder().apply(block).build()
    val converter = MoshiContentConverter(builder)
    register(ContentType.Application.Json, converter)
}
