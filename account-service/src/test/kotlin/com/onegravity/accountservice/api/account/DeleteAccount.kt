package com.onegravity.accountservice.api.account

import com.google.gson.Gson
import com.onegravity.accountservice.persistence.model.AccountStatus
import com.onegravity.accountservice.route.model.account.ResponseAccount
import com.onegravity.accountservice.util.testApps
import com.onegravity.util.getKoinInstance
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.ktor.http.*
import io.ktor.server.testing.*

@Suppress("unused")
class DeleteAccount : BehaviorSpec( {
    testApps(this) { testEngine, prefix ->
        val gson = getKoinInstance<Gson>()

        // create a test account
        val (newAccount, _) = createAccount(testEngine, AccountStatus.Active, gson)

        `when`("$prefix - I call DELETE /api/v1/admin/accounts/{accountUUID}") {
            val call = testEngine.handleRequest(HttpMethod.Delete, "/api/v1/admin/accounts/${newAccount.accountUUID}")
            val updatedAccount = gson.fromJson(call.response.content.toString(), ResponseAccount::class.java)

            then("the response status should be OK") {
                call.response.status() shouldBe HttpStatusCode.OK
            }

            then("accountUUID and createdAt should not have changed") {
                newAccount.accountUUID shouldBe updatedAccount.accountUUID
                newAccount.createdAt shouldBe updatedAccount.createdAt
            }

            then("the modified timestamp should have changed") {
                newAccount.modifiedAt shouldBeLessThan updatedAccount.modifiedAt
                updatedAccount.createdAt shouldBeLessThan updatedAccount.modifiedAt
            }

            then("the status should be Deleted") {
                updatedAccount.status shouldBe AccountStatus.Deleted
            }
        }

        `when`("$prefix - I call DELETE /api/v1/admin/accounts/{accountUUID} with an invalid uuid") {
            val call = testEngine.handleRequest(HttpMethod.Delete, "/api/v1/admin/accounts/123456")

            then("the response status should be HTTP 400 BadRequest") {
                call.response.status() shouldBe HttpStatusCode.BadRequest
            }
        }

        `when`("$prefix - I call PUT /api/v1/admin/accounts/{accountUUID} with a non existing uuid") {
            val call = testEngine.handleRequest(HttpMethod.Delete, "/api/v1/admin/accounts/00000000-0000-0000-0000-000000000000")

            then("the response status should be HTTP 404 NotFound") {
                call.response.status() shouldBe HttpStatusCode.NotFound
            }
        }
    }
} )
