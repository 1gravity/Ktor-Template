package com.onegravity.accountservice

import com.google.gson.Gson
import com.onegravity.config.Config
import com.onegravity.config.ConfigImpl
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.dsl.module as KoinModule

@ExperimentalSerializationApi
fun applicationModule(environment: ApplicationEnvironment, gson: Gson) =
    KoinModule {
        single { environment.log }      // org.slf4j.Logger
        single<Config> {
            val applicationConfig = HoconApplicationConfig(ConfigFactory.load())
            ConfigImpl(applicationConfig)
        }
        single { gson }
    }
