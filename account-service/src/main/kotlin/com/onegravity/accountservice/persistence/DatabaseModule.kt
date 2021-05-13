package com.onegravity.accountservice.persistence

import com.onegravity.accountservice.persistence.model.DaoProvider
import com.onegravity.accountservice.persistence.model.exposed.ExposedDaoProvider
import com.onegravity.accountservice.persistence.model.ktorm.KtormDaoProvider
import com.onegravity.config.DatabaseConfigImpl
import io.ktor.application.*
import org.ktorm.logging.LogLevel
import org.koin.dsl.module as KoinModule

@Suppress("MoveVariableDeclarationIntoWhen")
fun databaseModule(environment: ApplicationEnvironment) =
    KoinModule {
        val logLevel = if (environment.developmentMode) LogLevel.TRACE else LogLevel.WARN

        val orm = environment.config.propertyOrNull("ktor.database.orm")?.getString()
        val daoProvider = when (orm) {
            "EXPOSED" -> ExposedDaoProvider(DatabaseConfigImpl)
            else -> KtormDaoProvider(DatabaseConfigImpl, logLevel)
        }

        single<DaoProvider> { daoProvider }
    }
