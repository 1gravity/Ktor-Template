package com.onegravity.accountservice.api.customer

import com.github.michaelbull.result.runCatching
import com.google.gson.Gson
import com.onegravity.accountservice.api.account.createAccount
import com.onegravity.accountservice.persistence.model.AccountStatus
import com.onegravity.accountservice.persistence.model.CustomerStatus
import com.onegravity.accountservice.persistence.model.Language
import com.onegravity.accountservice.route.model.customer.ResponseCustomer
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
class UpdateCustomer : BehaviorSpec( {
    testApps(this) { testEngine, prefix ->
        val gson = getKoinInstance<Gson>()

        // create test account
        val (newAccount, _) = createAccount(testEngine, AccountStatus.Active, gson)

        // create test customer
        val (newCustomer, _) = createCustomer(testEngine, newAccount, CustomerStatus.Active, gson)
        val testCustomer = TestCustomer(
            newCustomer.customerUUID,
            newCustomer.createdAt,
            newCustomer.modifiedAt,
            newCustomer.status,
            newCustomer.firstName,
            newCustomer.lastName,
            newCustomer.language,
            newCustomer.account.accountUUID
        )

        `when`("$prefix - I call PUT /api/v1/admin/customers") {
            val (updatedCustomer, status) = updateCustomer(testEngine, testCustomer, gson)

            then("the updated customer should not be null") {
                updatedCustomer shouldNotBe null
            }

            then("the response status should be OK") {
                status shouldBe HttpStatusCode.OK
            }

            then("the response body should the same as the original customer") {
                testCustomer.customerUUID shouldBe updatedCustomer.customerUUID
                testCustomer.status shouldBe updatedCustomer.status
                testCustomer.firstName shouldBe updatedCustomer.firstName
                testCustomer.lastName shouldBe updatedCustomer.lastName
                testCustomer.language shouldBe updatedCustomer.language
                testCustomer.createdAt shouldBe updatedCustomer.createdAt
            }

            then("the modified timestamp should have changed") {
                testCustomer.modifiedAt shouldBeLessThan updatedCustomer.modifiedAt
                testCustomer.createdAt shouldBeLessThan updatedCustomer.modifiedAt
            }
        }

        val customer2Update = TestCustomer(testCustomer.customerUUID, Instant.now(), Instant.now(), CustomerStatus.Blocked, "Freddy", "Kruger", Language.de)

        `when`("$prefix - I call PUT /api/v1/admin/customers with updated fields") {
            val (updatedCustomer, status) = updateCustomer(testEngine, customer2Update, gson)

            then("the response status should be OK") {
                status shouldBe HttpStatusCode.OK
            }

            then("customerUUID, createdAt and the account should not have changed") {
                testCustomer.customerUUID shouldBe updatedCustomer.customerUUID
                testCustomer.createdAt shouldBe updatedCustomer.createdAt
                testCustomer.accountUUID shouldBe updatedCustomer.account.accountUUID
            }

            then("the modified timestamp should have changed") {
                testCustomer.modifiedAt shouldBeLessThan updatedCustomer.modifiedAt
                testCustomer.createdAt shouldBeLessThan updatedCustomer.modifiedAt
            }

            then("first name, last name, language and status should have changed") {
                customer2Update.status shouldBe updatedCustomer.status
                customer2Update.firstName shouldBe updatedCustomer.firstName
                customer2Update.lastName shouldBe updatedCustomer.lastName
                customer2Update.language shouldBe updatedCustomer.language
            }
        }

        `when`("$prefix - I call PUT /api/v1/admin/customers with an invalid customerUUID") {
            val customer = TestCustomer("123456", Instant.now(), Instant.now(), CustomerStatus.Blocked, "Freddy", "Kruger", Language.de)
            val (_, status) = updateCustomer(testEngine, customer, gson)

            then("the response status should be HTTP 400 BadRequest") {
                status shouldBe HttpStatusCode.BadRequest
            }
        }

        `when`("$prefix - I call PUT /api/v1/admin/customers with a non existing customerUUID") {
            val customer = TestCustomer("00000000-0000-0000-0000-000000000000", Instant.now(), Instant.now(), CustomerStatus.Blocked, "Freddy", "Kruger", Language.de)
            val (_, status) = updateCustomer(testEngine, customer, gson)

            then("the response status should be HTTP 404 NotFound") {
                status shouldBe HttpStatusCode.NotFound
            }
        }

        `when`("$prefix - I call PUT /api/v1/admin/customer with an invalid customer status") {
            val call = testEngine.handleRequest(HttpMethod.Put, "/api/v1/admin/customers") {
                setBody("{" +
                        "    \"customerUUID\": \"${testCustomer.customerUUID}\",\n" +
                        "    \"status\": \"NotActive\"\n" +
                        "}"
                )
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }

            then("the response status should be HTTP 400 BadRequest") {
                call.response.status() shouldBe HttpStatusCode.BadRequest
            }
        }

        `when`("$prefix - I call PUT /api/v1/admin/customer with an invalid language") {
            val call = testEngine.handleRequest(HttpMethod.Put, "/api/v1/admin/customers") {
                setBody("{" +
                        "    \"customerUUID\": \"${testCustomer.customerUUID}\",\n" +
                        "    \"language\": \"invalid_language\"\n" +
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

fun updateCustomer(testEngine: TestApplicationEngine, customer: TestCustomer, gson: Gson):
        Pair<ResponseCustomer, HttpStatusCode> {
    val call = testEngine.handleRequest(HttpMethod.Put, "/api/v1/admin/customers") {
        setBody(gson.toJson(customer))
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }

    val result = runCatching {
        gson.fromJson(call.response.content.toString(), ResponseCustomer::class.java)
    }

    val responseCustomer = result.component1()
    assertNotNull(responseCustomer)
    return Pair(responseCustomer, call.response.status() ?: HttpStatusCode.InternalServerError)
}
