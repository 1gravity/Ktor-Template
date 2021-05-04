package com.onegravity.accountservice.persistence

import com.onegravity.accountservice.persistence.database.DatabaseBaseImpl
import com.onegravity.accountservice.util.Config
import org.koin.core.component.inject
import org.ktorm.logging.LogLevel
import org.testcontainers.containers.PostgreSQLContainer

class TestDatabaseImpl(logLevel: LogLevel = LogLevel.WARN) : DatabaseBaseImpl(logLevel) {

    private val config by inject<Config>()

    private val dbContainer = PostgreSQLContainer<Nothing>("postgres:13.2").apply {
        withDatabaseName(config.getPropertyOrThrow("ktor.database.name"))
        withUsername(config.getPropertyOrThrow("ktor.database.user"))
        withPassword(config.getPropertyOrThrow("ktor.database.password"))
        start()
    }

    override val url: String = dbContainer.jdbcUrl

    override val driver: String = dbContainer.driverClassName

    override val user: String = dbContainer.username

    override val password: String = dbContainer.password

}
