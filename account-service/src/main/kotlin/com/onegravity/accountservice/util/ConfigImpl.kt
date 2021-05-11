package com.onegravity.accountservice.util

import io.github.cdimascio.dotenv.dotenv
import io.ktor.config.*

class ConfigImpl(private val config: ApplicationConfig) : Config {

    private val dotenv = dotenv()

    override fun getProperty(key: String, default: String): String =
        dotenv.get(key, config.propertyOrNull(key)?.getString() ?: default)

    override fun getProperty(key: String): String? =
        dotenv.get(key, config.propertyOrNull(key)?.getString())

    override fun getPropertyOrThrow(key: String) = getProperty(key) ?: throw IllegalStateException("Missing property $key")

}
