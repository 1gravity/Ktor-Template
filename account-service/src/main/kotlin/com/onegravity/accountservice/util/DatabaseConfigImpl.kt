package com.onegravity.accountservice.util

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object DatabaseConfigImpl : DatabaseConfig, KoinComponent {

    private val config: Config by inject()

    // e.g. "jdbc:postgresql://localhost:5432/account"
    override val url = "jdbc:postgresql://" +
            config.getPropertyOrThrow("DB_HOST") + ":" +
            config.getPropertyOrThrow("DB_PORT") + "/" +
            config.getPropertyOrThrow("DB_NAME")

    override val driver = config.getPropertyOrThrow("ktor.database.driver")

    override val userName = config.getPropertyOrThrow("DB_USER")

    override val password = config.getPropertyOrThrow("DB_PASSWORD")

}
