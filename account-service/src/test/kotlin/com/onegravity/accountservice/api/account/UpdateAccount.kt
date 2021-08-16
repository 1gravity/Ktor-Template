package com.onegravity.accountservice.api.account

import com.google.gson.Gson
import com.onegravity.accountservice.persistence.model.AccountStatus
import com.onegravity.accountservice.route.model.account.ResponseAccount
import com.onegravity.accountservice.util.testApps
import com.onegravity.util.getKoinInstance
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.http.*
import io.ktor.server.testing.*
import java.time.Instant
import kotlin.test.assertNotNull

@Suppress("unused")
class UpdateAccount : BehaviorSpec( {
    testApps(this) { testEngine, prefix ->
        val gson = getKoinInstance<Gson>()

        val (newAccount, _) = createAccount(testEngine, AccountStatus.Active, gson)

        `when`("$prefix - I call PUT /api/v1/admin/accounts/{accountUUID}") {
            val (updatedAccount, status) = updateAccount(gson, testEngine, newAccount)

            then("the updated account should not be null") {
                updatedAccount shouldNotBe null
            }

            then("the response status should be OK") {
                status shouldBe HttpStatusCode.OK
            }

            then("the response body should the same as the original account") {
                newAccount.accountUUID shouldBe updatedAccount.accountUUID
                newAccount.status shouldBe updatedAccount.status
                newAccount.createdAt shouldBe updatedAccount.createdAt
            }

            then("the modified timestamp should have changed") {
                newAccount.modifiedAt shouldBeLessThan updatedAccount.modifiedAt
                updatedAccount.createdAt shouldBeLessThan updatedAccount.modifiedAt
            }
        }

        val account2Update = ResponseAccount(newAccount.accountUUID, Instant.now(), Instant.now(), AccountStatus.Blocked)

        `when`("$prefix - I call PUT /api/v1/admin/accounts/{accountUUID} with updated fields") {
            val (updatedAccount, status) = updateAccount(gson, testEngine, account2Update)

            then("the updated account should not be null") {
                updatedAccount shouldNotBe null
            }

            then("the response status should be OK") {
                status shouldBe HttpStatusCode.OK
            }

            then("accountUUID and createdAt should not have changed") {
                newAccount.accountUUID shouldBe updatedAccount.accountUUID
                newAccount.createdAt shouldBe updatedAccount.createdAt
            }

            then("the modified timestamp should have changed") {
                newAccount.modifiedAt shouldBeLessThan updatedAccount.modifiedAt
                updatedAccount.createdAt shouldBeLessThan updatedAccount.modifiedAt
            }

            then("the status should have changed") {
                account2Update.status shouldBe updatedAccount.status
            }
        }

        `when`("$prefix - I call PUT /api/v1/admin/accounts/{accountUUID} with an invalid uuid") {
            val account = ResponseAccount("123456", Instant.now(), Instant.now(), AccountStatus.Blocked)
            val call = testEngine.handleRequest(HttpMethod.Put, "/api/v1/admin/accounts/123456") {
                setBody(gson.toJson(account))
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }

            then("the response status should be HTTP 400 BadRequest") {
                call.response.status() shouldBe HttpStatusCode.BadRequest
            }
        }

        `when`("$prefix - I call PUT /api/v1/admin/accounts/{accountUUID} with a non existing uuid") {
            val account = ResponseAccount("00000000-0000-0000-0000-000000000000", Instant.now(), Instant.now(), AccountStatus.Blocked)
            val call = testEngine.handleRequest(HttpMethod.Put, "/api/v1/admin/accounts/00000000-0000-0000-0000-000000000000") {
                setBody(gson.toJson(account))
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }

            then("the response status should be HTTP 404 NotFound") {
                call.response.status() shouldBe HttpStatusCode.NotFound
            }
        }

        `when`("$prefix - I call PUT /api/v1/admin/accounts/{accountUUID} with an invalid account status") {
            val call = testEngine.handleRequest(HttpMethod.Put, "/api/v1/admin/accounts/${newAccount.accountUUID}") {
                setBody("{" +
                        "    \"accountUUID\": \"${newAccount.accountUUID}\",\n" +
                        "    \"status\": \"NotActive\"\n" +
                        "}"
                )
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }

            then("the response status should be HTTP 400 BadRequest") {
                call.response.status() shouldBe HttpStatusCode.BadRequest
            }
        }
    }
} )

fun updateAccount(gson: Gson, testEngine: TestApplicationEngine, account: ResponseAccount): Pair<ResponseAccount, HttpStatusCode> {
    val call = testEngine.handleRequest(HttpMethod.Put, "/api/v1/admin/accounts/${account.accountUUID}") {
        setBody(gson.toJson(account))
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }

    val result = runCatching {
        gson.fromJson(call.response.content.toString(), ResponseAccount::class.java)
    }

    val responseAccount = result.getOrNull()
    assertNotNull(responseAccount)
    return Pair(responseAccount, call.response.status() ?: HttpStatusCode.InternalServerError)
}
