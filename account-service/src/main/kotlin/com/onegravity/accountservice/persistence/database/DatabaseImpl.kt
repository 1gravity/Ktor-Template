package com.onegravity.accountservice.persistence.database

import com.onegravity.accountservice.util.Config
import org.koin.core.component.inject
import org.ktorm.logging.LogLevel

class DatabaseImpl(logLevel: LogLevel = LogLevel.WARN) : DatabaseBaseImpl(logLevel) {

    private val config: Config by inject()

    // e.g. "jdbc:postgresql://localhost:5432/account"
    override val url = "jdbc:postgresql://" +
            config.getPropertyOrThrow("ktor.database.host") + ":" +
            config.getPropertyOrThrow("ktor.database.port") + "/" +
            config.getPropertyOrThrow("ktor.database.name")

    override val driver = config.getPropertyOrThrow("ktor.database.driver")

    override val user = config.getPropertyOrThrow("ktor.database.user")

    override val password = config.getPropertyOrThrow("ktor.database.password")

}
