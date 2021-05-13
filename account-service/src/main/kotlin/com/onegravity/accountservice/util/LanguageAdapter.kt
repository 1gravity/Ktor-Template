package com.onegravity.accountservice.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.onegravity.accountservice.persistence.model.Language
import com.onegravity.util.readEnum
import com.onegravity.util.writeEnum

object LanguageAdapter: TypeAdapter<Language>() {
    override fun write(writer: JsonWriter, value: Language) = writeEnum(writer, value)
    override fun read(reader: JsonReader) = readEnum<Language>(reader)
}
