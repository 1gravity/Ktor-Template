package com.onegravity.accountservice.application

import io.ktor.application.*
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.dsl.module as KoinModule

@ExperimentalSerializationApi
fun applicationModule(environment: ApplicationEnvironment) =
    KoinModule {
        single { environment.log }
    }
