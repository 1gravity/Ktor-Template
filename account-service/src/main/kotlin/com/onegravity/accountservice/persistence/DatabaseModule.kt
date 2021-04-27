package com.onegravity.accountservice.persistence

import com.onegravity.accountservice.persistence.database.Database
import com.onegravity.accountservice.persistence.database.DatabaseImpl
import io.ktor.application.*
import kotlinx.serialization.ExperimentalSerializationApi
import org.ktorm.logging.LogLevel
import org.koin.dsl.module as KoinModule

@ExperimentalSerializationApi
fun databaseModule(environment: ApplicationEnvironment) =
    KoinModule {
        val logLevel = if (environment.developmentMode) LogLevel.TRACE else LogLevel.WARN
        single<Database> { DatabaseImpl(logLevel) }
    }
