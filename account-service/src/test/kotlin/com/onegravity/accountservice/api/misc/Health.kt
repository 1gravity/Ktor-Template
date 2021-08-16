package com.onegravity.accountservice.api.misc

import com.google.gson.Gson
import com.onegravity.accountservice.util.*
import com.onegravity.config.Config
import com.onegravity.route.ServiceStatus
import com.onegravity.util.getKoinInstance
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldBeInteger
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.string.shouldMatch
import io.ktor.http.*
import io.ktor.server.testing.*

@Suppress("unused")
class HealthTest : BehaviorSpec({
    // GET /status
    testApp(this) { testEngine, _ ->
        val config = getKoinInstance<Config>()
        val gson = getKoinInstance<Gson>()

        `when`("I call GET /status") {
            val call = testEngine.handleRequest(HttpMethod.Get, "/status")

            then("the response status should be OK") {
                call.response.status() shouldBe HttpStatusCode.OK
            }

            val json = call.response.content
            val status = gson.fromJson(json.toString(), ServiceStatus::class.java)

            then("the response body should have the correct 'serviceName'") {
                status.serviceName shouldNotBe null
                status.serviceName shouldMatch config.getPropertyOrThrow("ktor.serviceName")
            }

            then("the response body should have a correct 'uptime' field") {
                status.uptime shouldNotBe null
                status.uptime shouldEndWith "s"
                status.uptime.dropLast(1).shouldBeInteger()
            }
        }
    }
})
