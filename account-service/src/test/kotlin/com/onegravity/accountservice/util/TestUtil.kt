package com.onegravity.accountservice.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.onegravity.accountservice.application.configureGson
import com.onegravity.accountservice.application.mainModule
import com.onegravity.accountservice.persistence.model.DaoProvider
import com.onegravity.accountservice.persistence.model.ktorm.KtormDaoProvider
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.scopes.GivenScope
import io.ktor.application.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import org.ktorm.logging.LogLevel
import io.ktor.server.testing.withTestApplication as ktorWithTestApplication

/**
 * Run the tests with both Ktorm and Exposed.
 */
fun testApps(
    spec: BehaviorSpec,
    test: suspend GivenScope.(TestApplicationEngine, prefix: String) -> Unit
) {
    testAppWithKtorm(spec, test)
    testAppWithExposed(spec, test)
}

/**
 * Run the tests only with Ktorm.
 */
fun testApp(
    spec: BehaviorSpec,
    test: suspend GivenScope.(TestApplicationEngine, prefix: String) -> Unit
) {
    testAppWithKtorm(spec, test)
}

private fun testAppWithKtorm(
    spec: BehaviorSpec,
    test: suspend GivenScope.(TestApplicationEngine, prefix: String) -> Unit
) {
    @OptIn(ExperimentalSerializationApi::class)
    val ktormApp: Application.() -> Unit = {
        mainModule().also {
            loadKoinModules(
                module {
                    single<DaoProvider>(override = true) { KtormDaoProvider(TestDatabaseConfigImpl, LogLevel.DEBUG) }
                }
            )
        }
    }

    spec.given("ktorm test application container") {
        withTestApplication(
            application = ktormApp,
            runTests = { this@given.test(this@withTestApplication, "ktorm") }
        )
    }
}

private fun testAppWithExposed(
    spec: BehaviorSpec,
    test: suspend GivenScope.(TestApplicationEngine, prefix: String) -> Unit
) {
    @OptIn(ExperimentalSerializationApi::class)
    val exposedApp: Application.() -> Unit = {
        mainModule().also {
            loadKoinModules(
                module {
                    single<DaoProvider>(override = true) { KtormDaoProvider(TestDatabaseConfigImpl, LogLevel.DEBUG) }
                }
            )
        }
    }

    spec.given("exposed test application container") {
        withTestApplication(
            application = exposedApp,
            runTests = { this@given.test(this@withTestApplication, "exposed") }
        )
    }
}

@Suppress("BlockingMethodInNonBlockingContext")
private fun withTestApplication(
    application: Application.() -> Unit,
    runTests: suspend TestApplicationEngine.() -> Unit
) {
    ktorWithTestApplication(application) {
        runBlocking {
            // runTests is the same as runTests(this@ktorWithTestApplication)
            runTests()
            stopKoin()
        }
    }
}

 val gson: Gson = GsonBuilder()
    .apply { configureGson(this) }
    .create()
