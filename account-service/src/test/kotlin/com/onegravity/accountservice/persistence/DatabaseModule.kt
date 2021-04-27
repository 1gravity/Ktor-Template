package com.onegravity.accountservice.persistence

import com.onegravity.accountservice.persistence.database.Database
import io.ktor.application.*
import kotlinx.serialization.ExperimentalSerializationApi
import org.ktorm.logging.LogLevel
import org.koin.dsl.module as KoinModule

@ExperimentalSerializationApi
fun databaseModule(environment: ApplicationEnvironment) =
    KoinModule {
        single<Database> { TestDatabaseImpl(LogLevel.WARN) }
    }
