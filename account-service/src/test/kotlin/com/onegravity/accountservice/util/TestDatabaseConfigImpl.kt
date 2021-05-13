package com.onegravity.accountservice.util

import com.onegravity.config.Config
import com.onegravity.config.DatabaseConfig
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.testcontainers.containers.PostgreSQLContainer

object TestDatabaseConfigImpl : DatabaseConfig, KoinComponent {

    private val config: Config by inject()

    private val dbContainer = PostgreSQLContainer<Nothing>("postgres:13.2").apply {
        withDatabaseName(config.getPropertyOrThrow("DB_NAME"))
        withUsername(config.getPropertyOrThrow("DB_USER"))
        withPassword(config.getPropertyOrThrow("DB_PASSWORD"))
        start()
    }

    override val url: String = dbContainer.jdbcUrl

    override val driver: String = dbContainer.driverClassName

    override val userName: String = dbContainer.username

    override val password: String = dbContainer.password

}
