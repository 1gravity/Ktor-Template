package com.onegravity.accountservice.persistence

import com.onegravity.accountservice.persistence.database.DatabaseBaseImpl
import com.onegravity.accountservice.util.getPropertyOrThrow
import org.ktorm.logging.LogLevel
import org.testcontainers.containers.PostgreSQLContainer

class TestDatabaseImpl(logLevel: LogLevel = LogLevel.WARN) : DatabaseBaseImpl(logLevel) {

    private val dbContainer = PostgreSQLContainer<Nothing>("postgres:13.2").apply {
        withDatabaseName(getPropertyOrThrow("ktor.database.name"))
        withUsername(getPropertyOrThrow("ktor.database.user"))
        withPassword(getPropertyOrThrow("ktor.database.password"))
        start()
    }

    override val url: String = dbContainer.jdbcUrl

    override val driver: String = dbContainer.driverClassName

    override val user: String = dbContainer.username

    override val password: String = dbContainer.password

}
