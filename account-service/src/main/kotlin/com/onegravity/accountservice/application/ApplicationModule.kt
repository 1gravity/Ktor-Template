package com.onegravity.accountservice.application

import com.onegravity.accountservice.util.Config
import com.onegravity.accountservice.util.ConfigImpl
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.dsl.module as KoinModule

@ExperimentalSerializationApi
fun applicationModule(environment: ApplicationEnvironment) =
    KoinModule {
        single { environment.log }      // org.slf4j.Logger
        single<Config> {
            val applicationConfig = HoconApplicationConfig(ConfigFactory.load())
            ConfigImpl(applicationConfig)
        }
    }
