package com.onegravity.accountservice.util

import com.typesafe.config.ConfigFactory
import io.ktor.config.*

private val config by lazy { HoconApplicationConfig(ConfigFactory.load()) }

fun getProperty(key: String, default: String) = config.propertyOrNull(key)?.getString() ?: default

fun getProperty(key: String) = config.propertyOrNull(key)?.getString()

fun getPropertyOrThrow(key: String) = getProperty(key) ?: throw IllegalStateException("Missing property $key")

