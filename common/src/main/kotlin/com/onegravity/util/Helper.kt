@file:Suppress("unused")

package com.onegravity.util

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

inline fun <reified T> getKoinInstance() =
    object : KoinComponent {
        val value: T by inject()
    }.value

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
    }.getOrNull()

inline fun <T1: Any, T2: Any, R: Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2)->R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

inline fun <T1: Any, T2: Any, T3: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3)->R?): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}

inline fun <T1: Any, T2: Any, T3: Any, T4: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, block: (T1, T2, T3, T4)->R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}

inline fun <T1: Any, T2: Any, T3: Any, T4: Any, T5: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, p5: T5?, block: (T1, T2, T3, T4, T5)->R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null) block(p1, p2, p3, p4, p5) else null
}
