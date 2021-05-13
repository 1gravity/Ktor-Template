package com.onegravity.adapter

import com.squareup.moshi.*
import java.time.Instant

object MoshiInstantAdapter : JsonAdapter<Instant>() {
    @FromJson
    override fun fromJson(reader: JsonReader): Instant = Instant.parse(reader.nextString())

    @ToJson
    override fun toJson(writer: JsonWriter, value: Instant?) {
        writer.value(value.toString())
    }
}
