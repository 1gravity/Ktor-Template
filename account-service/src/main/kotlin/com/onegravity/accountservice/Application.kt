package com.onegravity.accountservice

import com.onegravity.accountservice.controller.controllerModule
import com.onegravity.accountservice.persistence.databaseModule
import com.onegravity.accountservice.route.accountRouting
import com.onegravity.accountservice.route.customerRouting
import com.onegravity.accountservice.util.gson
import com.onegravity.module.errorModule
import com.onegravity.module.headerModule
import com.onegravity.module.openAPIGenerator
import com.onegravity.route.healthRouting
import com.papsign.ktor.openapigen.route.apiRouting
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import io.ktor.server.netty.EngineMain as NettyEngine

fun main(args: Array<String>) = NettyEngine.main(args)

@OptIn(ExperimentalSerializationApi::class)
val defaultDI = fun(environment: ApplicationEnvironment) {
    startKoin {
        modules(applicationModule(environment, gson))
    }
    // there's a dependency between these two modules so we need to load them sequentially
    loadKoinModules(databaseModule(environment))
    loadKoinModules(controllerModule)
}

/**
 * The main entry point into the app (mainModule is configured in application.conf).
 */
@Suppress("unused") // Referenced in application.conf
@ExperimentalStdlibApi
@ExperimentalSerializationApi
fun Application.mainModule(di: (environment: ApplicationEnvironment) -> Unit = defaultDI) {
    // setup dependency injection
    di(environment)

    install(ContentNegotiation) {
        val converter = GsonConverter(gson)
        register(ContentType.Application.Json, converter)
    }

    // default error responses
    errorModule()

    // some default headers
    headerModule()

    // install OpenAPI generator
    openAPIGenerator()

    apiRouting {
        healthRouting()
        customerRouting()
        accountRouting()
    }

    if (environment.developmentMode) {
        // log all calls to this service
        install(CallLogging)
    }
}
