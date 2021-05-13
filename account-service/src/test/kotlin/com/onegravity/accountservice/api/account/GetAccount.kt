package com.onegravity.accountservice.api.account

import com.google.gson.Gson
import com.onegravity.accountservice.persistence.model.AccountStatus
import com.onegravity.accountservice.route.model.account.ResponseAccount
import com.onegravity.accountservice.util.testApps
import com.onegravity.util.getKoinInstance
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.*
import io.ktor.server.testing.*

@Suppress("unused")
class GetAccount : BehaviorSpec( {
    testApps(this) { testEngine, prefix ->
        val gson = getKoinInstance<Gson>()

        // create a test account
        val (newAccount, _) = createAccount(testEngine, AccountStatus.Active, gson)

        `when`("$prefix - I call GET /api/v1/admin/accounts/{accountUUID}") {
            val call = testEngine.handleRequest(HttpMethod.Get, "/api/v1/admin/accounts/${newAccount.accountUUID}")

            then("the response status should be OK") {
                call.response.status() shouldBe HttpStatusCode.OK
            }

            then("the response body should contain the newly created account") {
                val account = gson.fromJson(call.response.content.toString(), ResponseAccount::class.java)
                account shouldBe newAccount
            }
        }

        `when`("$prefix - I call GET /api/v1/admin/accounts/{accountUUID} with an invalid uuid") {
            val call = testEngine.handleRequest(HttpMethod.Get, "/api/v1/admin/accounts/123456")

            then("the response status should be HTTP 400 BadRequest") {
                call.response.status() shouldBe HttpStatusCode.BadRequest
            }
        }

        `when`("$prefix - I call GET /api/v1/admin/accounts/{accountUUID} with a non existing uuid") {
            val call = testEngine.handleRequest(HttpMethod.Get, "/api/v1/admin/accounts/00000000-0000-0000-0000-000000000000")

            then("the response status should be HTTP 404 NotFound") {
                call.response.status() shouldBe HttpStatusCode.NotFound
            }
        }
    }
} )
