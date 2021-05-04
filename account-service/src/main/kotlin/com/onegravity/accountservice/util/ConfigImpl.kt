package com.onegravity.accountservice.util

import io.ktor.config.*

class ConfigImpl(private val config: ApplicationConfig) : Config {

    override fun getProperty(key: String, default: String) = config.propertyOrNull(key)?.getString() ?: default

    override fun getProperty(key: String) = config.propertyOrNull(key)?.getString()

    override fun getPropertyOrThrow(key: String) = getProperty(key) ?: throw IllegalStateException("Missing property $key")

}
