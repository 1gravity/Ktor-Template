package com.onegravity.accountservice.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.onegravity.accountservice.persistence.model.AccountStatus
import com.onegravity.util.readEnum
import com.onegravity.util.writeEnum

object AccountStatusAdapter: TypeAdapter<AccountStatus>() {
    override fun write(writer: JsonWriter, value: AccountStatus) = writeEnum(writer, value)
    override fun read(reader: JsonReader) = readEnum<AccountStatus>(reader)
}
