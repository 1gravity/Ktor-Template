package com.onegravity.accountservice.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.onegravity.accountservice.application.mainModule
import com.onegravity.accountservice.controller.adapters.GsonInstantAdapter
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.scopes.GivenScope
import io.ktor.application.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.core.context.GlobalContext.stopKoin
import java.time.Instant

@OptIn(ExperimentalSerializationApi::class)
private val application: Application.() -> Unit = { mainModule() }

fun withTestApplication(
    mainApplication: Application.() -> Unit,
    test: suspend TestApplicationEngine.() -> Unit
) = io.ktor.server.testing.withTestApplication(mainApplication) {

    runBlocking {
        test()
        stopKoin()
    }
}

fun BehaviorSpec.testApplication(test: suspend GivenScope.(TestApplicationEngine) -> Unit) {
    given("test application container") {
        withTestApplication(application) {
            this@given.test(this@withTestApplication)
      }
    }
}

val gson: Gson = GsonBuilder()
    .apply {
        setPrettyPrinting()
        disableHtmlEscaping()
        registerTypeAdapter(Instant::class.java, GsonInstantAdapter)
    }
    .create()
