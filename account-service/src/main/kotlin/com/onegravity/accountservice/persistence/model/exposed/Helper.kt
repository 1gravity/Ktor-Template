package com.onegravity.accountservice.persistence.model.exposed

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}

inline fun <reified T : Enum<T>> Table.postgresEnumeration(
    columnName: String,
    postgresEnumName: String
) = customEnumeration(
    name = columnName,
    sql = postgresEnumName,
    fromDb = { value -> enumValueOf<T>(value as String) },
    toDb = { PGEnum(postgresEnumName, it) }
)
