package com.onegravity.accountservice.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.onegravity.accountservice.persistence.model.CustomerStatus
import com.onegravity.util.readEnum
import com.onegravity.util.writeEnum

object CustomerStatusAdapter: TypeAdapter<CustomerStatus>() {
    override fun write(writer: JsonWriter, value: CustomerStatus) = writeEnum(writer, value)
    override fun read(reader: JsonReader) = readEnum<CustomerStatus>(reader)
}
