package com.onegravity.accountservice.util

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.testcontainers.containers.PostgreSQLContainer

object TestDatabaseConfigImpl : DatabaseConfig, KoinComponent {

    private val config: Config by inject()

    private val dbContainer = PostgreSQLContainer<Nothing>("postgres:13.2").apply {
        withDatabaseName(config.getPropertyOrThrow("ktor.database.name"))
        withUsername(config.getPropertyOrThrow("ktor.database.user"))
        withPassword(config.getPropertyOrThrow("ktor.database.password"))
        start()
    }

    override val url: String = dbContainer.jdbcUrl

    override val driver: String = dbContainer.driverClassName

    override val userName: String = dbContainer.username

    override val password: String = dbContainer.password

}
