package com.onegravity.accountservice.application

import com.onegravity.accountservice.controller.adapters.GsonInstantAdapter
import com.onegravity.accountservice.controller.controllerModule
import com.onegravity.accountservice.persistence.databaseModule
import com.onegravity.accountservice.persistence.repository.repositoryModule
import com.onegravity.accountservice.route.accountRouting
import com.onegravity.accountservice.route.customerRouting
import com.onegravity.accountservice.route.healthRouting
import com.papsign.ktor.openapigen.route.apiRouting
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.core.context.startKoin
import java.time.Instant
import io.ktor.server.netty.EngineMain as NettyEngine

fun main(args: Array<String>) = NettyEngine.main(args)

/**
 * The main entry point into the app (mainModule is configured in application.conf).
 */
@Suppress("unused") // Referenced in application.conf
@ExperimentalStdlibApi
@ExperimentalSerializationApi
fun Application.mainModule() {
    // setup dependency injection
    startKoin {
        modules(
            applicationModule(environment),
            databaseModule(environment),
            controllerModule,
            repositoryModule
        )
    }

    // some default headers
    install(DefaultHeaders) {
        header(HttpHeaders.Server, "N/A for security reasons")
        header(HttpHeaders.CacheControl, "no-cache, no-store, must-revalidate")
        header(HttpHeaders.Pragma, "no-cache")
        header(HttpHeaders.Expires, "0")
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            disableHtmlEscaping()
            registerTypeAdapter(Instant::class.java, GsonInstantAdapter)
        }
    }

    // default error responses
    errorModule()

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
