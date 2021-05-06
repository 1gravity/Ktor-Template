package com.onegravity.accountservice.api.customer

import com.github.michaelbull.result.runCatching
import com.onegravity.accountservice.api.account.createAccount
import com.onegravity.accountservice.persistence.model.account.AccountStatus
import com.onegravity.accountservice.persistence.model.customer.CustomerStatus
import com.onegravity.accountservice.persistence.model.customer.Language
import com.onegravity.accountservice.route.model.customer.ResponseCustomer
import com.onegravity.accountservice.util.gson
import com.onegravity.accountservice.util.testApplication
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
    testApplication { testEngine ->
        // create test account
        val (newAccount, _) = createAccount(testEngine, AccountStatus.Active)

        // create test customer
        val (newCustomer, _) = createCustomer(testEngine, newAccount!!, CustomerStatus.Active)
        assertNotNull(newCustomer)
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

        `when`("I call PUT /api/v1/admin/customers") {
            val (updatedCustomer, status) = updateCustomer(testEngine, testCustomer)

            then("the updated customer should not be null") {
                updatedCustomer shouldNotBe null
            }

            then("the response status should be OK") {
                status shouldBe HttpStatusCode.OK
            }

            then("the response body should the same as the original customer") {
                testCustomer.customerUUID shouldBe updatedCustomer!!.customerUUID
                testCustomer.status shouldBe updatedCustomer.status
                testCustomer.firstName shouldBe updatedCustomer.firstName
                testCustomer.lastName shouldBe updatedCustomer.lastName
                testCustomer.language shouldBe updatedCustomer.language
                testCustomer.createdAt shouldBe updatedCustomer.createdAt
            }

            then("the modified timestamp should have changed") {
                testCustomer.modifiedAt shouldBeLessThan updatedCustomer!!.modifiedAt
                testCustomer.createdAt shouldBeLessThan updatedCustomer.modifiedAt
            }
        }

        val customer2Update = TestCustomer(testCustomer.customerUUID, Instant.now(), Instant.now(), CustomerStatus.Blocked, "Freddy", "Kruger", Language.de)

        `when`("I call PUT /api/v1/admin/customers with updated fields") {
            val (updatedCustomer, status) = updateCustomer(testEngine, customer2Update)
            assertNotNull(updatedCustomer)

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

        `when`("I call PUT /api/v1/admin/customers with an invalid customerUUID") {
            val customer = TestCustomer("123456", Instant.now(), Instant.now(), CustomerStatus.Blocked, "Freddy", "Kruger", Language.de)
            val (_, status) = updateCustomer(testEngine, customer)

            then("the response status should be HTTP 400 BadRequest") {
                status shouldBe HttpStatusCode.BadRequest
            }
        }

        `when`("I call PUT /api/v1/admin/customers with a non existing customerUUID") {
            val customer = TestCustomer("00000000-0000-0000-0000-000000000000", Instant.now(), Instant.now(), CustomerStatus.Blocked, "Freddy", "Kruger", Language.de)
            val (_, status) = updateCustomer(testEngine, customer)

            then("the response status should be HTTP 404 NotFound") {
                status shouldBe HttpStatusCode.NotFound
            }
        }
    }
} )

fun updateCustomer(testEngine: TestApplicationEngine, customer: TestCustomer): Pair<ResponseCustomer?, HttpStatusCode> {
    val call = testEngine.handleRequest(HttpMethod.Put, "/api/v1/admin/customers") {
        setBody(gson.toJson(customer))
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }

    val result = runCatching {
        gson.fromJson(call.response.content.toString(), ResponseCustomer::class.java)
    }
    return Pair(result.component1(), call.response.status() ?: HttpStatusCode.InternalServerError)
}