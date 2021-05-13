package com.onegravity.module

import com.onegravity.util.*
import com.onegravity.util.NotFoundException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import org.slf4j.Logger

fun Application.errorModule() {

    val logger = getKoinInstance<Logger>()

    suspend fun PipelineContext<Unit, ApplicationCall>.send(e: Throwable, code: HttpStatusCode) {
        logger.error("Exception occurred: ${e.message}")
        val response = TextContent(e.message ?: code.description,
            ContentType.Text.Plain.withCharset(Charsets.UTF_8),
            code
        )
        call.respond(response)
    }

    install(StatusPages) {
        status(HttpStatusCode.NotFound) {
            call.respond(TextContent("${it.value} ${it.description}", ContentType.Text.Plain.withCharset(Charsets.UTF_8), it))
        }

        // Http 400 Bad Request
        exception<ValidationException> { send(it, HttpStatusCode.BadRequest) }
        exception<IllegalArgumentException> { send(it, HttpStatusCode.BadRequest) }

        // HTTP 401 Unauthorized
        exception<IllegalAccessException> { send(it, HttpStatusCode.Unauthorized) }

        // HTTP 404 Not Found
        exception<NotFoundException> { send(it, HttpStatusCode.NotFound) }

        // HTTP 503 Service Unavailable
        exception<ServiceUnavailable> { send(it, HttpStatusCode.ServiceUnavailable) }

        // HTTP 500 Internal Server Error
        exception<Throwable> { send(it, HttpStatusCode.InternalServerError) }
    }
}
