package com.onegravity.accountservice.controller.adapters

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.runCatching
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.onegravity.accountservice.util.ValidationException
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
        runCatching {
            Instant.parse(reader.nextString())
        }.onFailure {
            throw ValidationException(it.message ?: it.javaClass.simpleName)
        }.component1()

}
