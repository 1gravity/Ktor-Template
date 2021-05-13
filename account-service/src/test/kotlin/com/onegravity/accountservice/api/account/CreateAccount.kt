package com.onegravity.accountservice.api.account

import com.google.gson.Gson
import com.onegravity.accountservice.persistence.model.AccountStatus
import com.onegravity.accountservice.util.testApps
import com.onegravity.util.getKoinInstance
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.http.*
import io.ktor.server.testing.*
import java.util.*

@Suppress("unused")
class CreateAccount : BehaviorSpec( {
    testApps(this) { testEngine, prefix ->
        val gson = getKoinInstance<Gson>()

        `when`("$prefix - I call POST /api/v1/admin/accounts") {
            val (account, status) = createAccount(testEngine, AccountStatus.Active, gson)

            then("the response mustn't be null") {
                account shouldNotBe null
            }

            then("the response status should be OK") {
                status shouldBe HttpStatusCode.OK
            }

            then("the response body should be the created account") {
                verifyAccount(account)
                account.status shouldBe AccountStatus.Active
                account.createdAt shouldBe account.modifiedAt
            }
        }

        `when`("$prefix - I call POST /api/v1/admin/accounts to create a deleted account") {
            val (account, status) = createAccount(testEngine, AccountStatus.Deleted, gson)

            then("the response mustn't be null") {
                account shouldNotBe null
            }

            then("the response status should be OK") {
                status shouldBe HttpStatusCode.OK
            }

            then("the returned account object should have status deleted") {
                account.status shouldBe AccountStatus.Deleted
                account.createdAt shouldBe account.modifiedAt
            }
        }

        `when`("$prefix - I call POST /api/v1/admin/accounts with an invalid account status") {
            val call = testEngine.handleRequest(HttpMethod.Post, "/api/v1/admin/accounts") {
                setBody(
                    "{" +
                            "    \"accountUUID\": \"${UUID.randomUUID()}\",\n" +
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
