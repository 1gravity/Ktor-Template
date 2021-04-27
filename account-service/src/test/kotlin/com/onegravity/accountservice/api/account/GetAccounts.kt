package com.onegravity.accountservice.api.account

import com.google.gson.reflect.TypeToken
import com.onegravity.accountservice.persistence.model.account.AccountStatus
import com.onegravity.accountservice.route.response.Account
import com.onegravity.accountservice.util.gson
import com.onegravity.accountservice.util.testApplication
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.http.*
import io.ktor.server.testing.*

@Suppress("unused")
class GetAccounts : BehaviorSpec( {
    testApplication { testEngine ->
        // create 10 test accounts
        repeat(10) {
            createAccount(testEngine, AccountStatus.Active)
        }

        `when`("I call GET /api/v1/admin/accounts") {
            val call = testEngine.handleRequest(HttpMethod.Get, "/api/v1/admin/accounts")

            then("the response status should be OK") {
                call.response.status() shouldBe HttpStatusCode.OK
            }

            then("the response body should contain a list of accounts") {
                val json = call.response.content

                val collectionType = object : TypeToken<Collection<Account>>() {}.type
                val accounts = gson.fromJson<List<Account>>(json.toString(), collectionType)

                accounts.isEmpty() shouldNotBe true

                accounts.forEach { verifyAccount(it) }
            }
        }
    }
} )
