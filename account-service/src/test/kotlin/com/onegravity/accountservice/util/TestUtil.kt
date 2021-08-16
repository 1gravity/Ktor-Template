@file:OptIn(ExperimentalSerializationApi::class)

package com.onegravity.accountservice.util

import com.onegravity.accountservice.applicationModule
import com.onegravity.accountservice.controller.controllerModule
import com.onegravity.accountservice.mainModule
import com.onegravity.accountservice.persistence.model.DaoProvider
import com.onegravity.accountservice.persistence.model.exposed.ExposedDaoProvider
import com.onegravity.accountservice.persistence.model.ktorm.KtormDaoProvider
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.scopes.BehaviorSpecGivenContainerContext as Context
import io.ktor.application.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.context.startKoin
import org.koin.dsl.module

private fun Application.ktormApp() {
    mainModule {
        testDI { KtormDaoProvider(TestDatabaseConfigImpl) }
    }
}

private fun Application.exposeApp() {
    mainModule {
        testDI { ExposedDaoProvider(TestDatabaseConfigImpl) }
    }
}

private fun Application.testDI(daoProvider: () -> DaoProvider) {
    startKoin {
        modules(applicationModule(environment, gson))
    }
    // there's a dependency between these two modules so we need to load them sequentially
    loadKoinModules(module {
        single { daoProvider() }
    })
    loadKoinModules(controllerModule)
}

/**
 * Run the tests with both Ktorm and Exposed.
 */
fun testApps(
    spec: BehaviorSpec,
    test: suspend Context.(TestApplicationEngine, prefix: String) -> Unit
) {
    testApp( { ktormApp() }, "ktorm", spec, test)
    testApp( { exposeApp() }, "exposed", spec, test)
}

/**
 * Run the tests only with Ktorm.
 */
fun testApp(
    spec: BehaviorSpec,
    test: suspend Context.(TestApplicationEngine, prefix: String) -> Unit
) {
    testApp( { ktormApp() }, "ktorm", spec, test)
}

private fun testApp(
    app: Application.() -> Unit,
    name: String,
    spec: BehaviorSpec,
    test: suspend Context.(TestApplicationEngine, prefix: String) -> Unit
) {
    spec.given("$name test application container") {
        withTestApplication(app) {
            runBlocking {
                test(this@withTestApplication, name)
                stopKoin()
            }
        }
    }
}
