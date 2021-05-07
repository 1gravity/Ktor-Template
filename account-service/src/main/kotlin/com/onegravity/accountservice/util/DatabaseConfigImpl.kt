package com.onegravity.accountservice.util

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object DatabaseConfigImpl : DatabaseConfig, KoinComponent {

    private val config: Config by inject()

    // e.g. "jdbc:postgresql://localhost:5432/account"
    override val url = "jdbc:postgresql://" +
            config.getPropertyOrThrow("ktor.database.host") + ":" +
            config.getPropertyOrThrow("ktor.database.port") + "/" +
            config.getPropertyOrThrow("ktor.database.name")

    override val driver = config.getPropertyOrThrow("ktor.database.driver")

    override val userName = config.getPropertyOrThrow("ktor.database.user")

    override val password = config.getPropertyOrThrow("ktor.database.password")

}
