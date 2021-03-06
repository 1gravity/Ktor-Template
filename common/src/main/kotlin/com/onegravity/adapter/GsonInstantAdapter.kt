package com.onegravity.adapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.onegravity.util.ValidationException
import java.time.Instant

object GsonInstantAdapter: TypeAdapter<Instant>() {
    override fun write(writer: JsonWriter, value: Instant?) {
        runCatching {
            writer.value(value.toString())
        }.onFailure {
            throw ValidationException(it.message ?: it.javaClass.simpleName)
        }
    }

    override fun read(reader: JsonReader) =
        runCatching{ Instant.parse(reader.nextString()) }
            .onFailure { throw ValidationException(it.message ?: it.javaClass.simpleName) }
            .getOrNull()

}
