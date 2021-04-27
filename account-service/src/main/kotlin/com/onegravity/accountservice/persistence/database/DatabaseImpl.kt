package com.onegravity.accountservice.persistence.database

import com.onegravity.accountservice.util.getPropertyOrThrow
import org.ktorm.logging.LogLevel

class DatabaseImpl(logLevel: LogLevel = LogLevel.WARN) : DatabaseBaseImpl(logLevel) {

    // e.g. "jdbc:postgresql://localhost:5432/account"
    override val url = "jdbc:postgresql://" +
            getPropertyOrThrow("ktor.database.host") + ":" +
            getPropertyOrThrow("ktor.database.port") + "/" +
            getPropertyOrThrow("ktor.database.name")

    override val driver = getPropertyOrThrow("ktor.database.driver")

    override val user = getPropertyOrThrow("ktor.database.user")

    override val password = getPropertyOrThrow("ktor.database.password")

}
