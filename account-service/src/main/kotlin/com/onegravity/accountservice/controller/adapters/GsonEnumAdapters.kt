package com.onegravity.accountservice.controller.adapters

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.runCatching
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.onegravity.accountservice.persistence.model.AccountStatus
import com.onegravity.accountservice.persistence.model.CustomerStatus
import com.onegravity.accountservice.persistence.model.Language
import com.onegravity.accountservice.util.ValidationException

/**
 * Gson converts an invalid enum value to Null instead of throwing an exception.
 * To remedy that we need custom adapters.
 */
fun <T: Enum<T>> writeEnum(writer: JsonWriter, value: T) {
    runCatching {
        writer.value(value.name)
    }.onFailure {
        throw ValidationException(it.message ?: it.javaClass.simpleName)
    }
}

inline fun <reified T: Enum<T>> readEnum(reader: JsonReader) =
    runCatching {
        enumValueOf<T>(reader.nextString())
    }.onFailure {
        throw ValidationException(it.message ?: it.javaClass.simpleName)
    }.component1()

object AccountStatusAdapter: TypeAdapter<AccountStatus>() {
    override fun write(writer: JsonWriter, value: AccountStatus) = writeEnum(writer, value)
    override fun read(reader: JsonReader) = readEnum<AccountStatus>(reader)
}

object CustomerStatusAdapter: TypeAdapter<CustomerStatus>() {
    override fun write(writer: JsonWriter, value: CustomerStatus) = writeEnum(writer, value)
    override fun read(reader: JsonReader) = readEnum<CustomerStatus>(reader)
}

object LanguageAdapter: TypeAdapter<Language>() {
    override fun write(writer: JsonWriter, value: Language) = writeEnum(writer, value)
    override fun read(reader: JsonReader) = readEnum<Language>(reader)
}
