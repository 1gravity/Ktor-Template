package com.onegravity

fun Map<String, String>.toStringList(): List<String> {
    val result = ArrayList<String>()
    keys.forEach {
        result.add("$it:${this[it]}")
    }
    return result
}
