package com.onegravity.accountservice.application

import com.google.gson.GsonBuilder
import com.onegravity.accountservice.controller.adapters.AccountStatusAdapter
import com.onegravity.accountservice.controller.adapters.CustomerStatusAdapter
import com.onegravity.accountservice.controller.adapters.GsonInstantAdapter
import com.onegravity.accountservice.controller.adapters.LanguageAdapter
import com.onegravity.accountservice.controller.controllerModule
import com.onegravity.accountservice.persistence.databaseModule
import com.onegravity.accountservice.persistence.model.AccountStatus
import com.onegravity.accountservice.persistence.model.CustomerStatus
import com.onegravity.accountservice.persistence.model.Language
import com.onegravity.accountservice.route.accountRouting
import com.onegravity.accountservice.route.customerRouting
import com.onegravity.accountservice.route.healthRouting
import com.papsign.ktor.openapigen.route.apiRouting
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import java.time.Instant
import io.ktor.server.netty.EngineMain as NettyEngine

fun main(args: Array<String>) = NettyEngine.main(args)

@OptIn(ExperimentalSerializationApi::class)
val defaultDI = fun(environment: ApplicationEnvironment) {
    startKoin {
        modules(applicationModule(environment))
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

    // some default headers
    install(DefaultHeaders) {
        header(HttpHeaders.Server, "N/A for security reasons")
        header(HttpHeaders.CacheControl, "no-cache, no-store, must-revalidate")
        header(HttpHeaders.Pragma, "no-cache")
        header(HttpHeaders.Expires, "0")
    }

    install(ContentNegotiation) {
        gson { configureGson(this) }
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

fun configureGson(builder: GsonBuilder) {
    with(builder) {
        setPrettyPrinting()
        disableHtmlEscaping()
        registerTypeAdapter(Instant::class.java, GsonInstantAdapter)
        registerTypeAdapter(AccountStatus::class.java, AccountStatusAdapter)
        registerTypeAdapter(CustomerStatus::class.java, CustomerStatusAdapter)
        registerTypeAdapter(Language::class.java, LanguageAdapter)
    }
}