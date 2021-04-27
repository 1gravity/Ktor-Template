package com.onegravity.accountservice.api.customer

import com.github.michaelbull.result.runCatching
import com.onegravity.accountservice.api.account.createAccount
import com.onegravity.accountservice.persistence.model.account.AccountStatus
import com.onegravity.accountservice.persistence.model.customer.CustomerStatus
import com.onegravity.accountservice.persistence.model.customer.Language
import com.onegravity.accountservice.route.response.Account
import com.onegravity.accountservice.route.response.Customer
import com.onegravity.accountservice.util.gson
import com.onegravity.accountservice.util.testApplication
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.http.*
import io.ktor.server.testing.*
import java.time.Instant
import java.util.*

@Suppress("unused")
class UpdateCustomer : BehaviorSpec( {
    testApplication { testEngine ->
        // create test account
        val (newAccount, _) = createAccount(testEngine, AccountStatus.Active)

        // create test customer
        val (newCustomer, _) = createCustomer(testEngine, newAccount!!, CustomerStatus.Active)

        `when`("I call PUT /api/v1/admin/customers/{customerUUID}") {
            val (updatedCustomer, status) = updateCustomer(testEngine, newCustomer!!.customerUUID, newCustomer)

            then("the updated customer should not be null") {
                updatedCustomer shouldNotBe null
            }

            then("the response status should be OK") {
                status shouldBe HttpStatusCode.OK
            }

            then("the response body should the same as the original customer") {
                newCustomer.customerUUID shouldBe updatedCustomer!!.customerUUID
                newCustomer.status shouldBe updatedCustomer.status
                newCustomer.firstName shouldBe updatedCustomer.firstName
                newCustomer.lastName shouldBe updatedCustomer.lastName
                newCustomer.language shouldBe updatedCustomer.language
                newCustomer.createdAt shouldBe updatedCustomer.createdAt
            }

            then("the modified timestamp should have changed") {
                newCustomer.modifiedAt shouldBeLessThan updatedCustomer!!.modifiedAt
                newCustomer.createdAt shouldBeLessThan updatedCustomer.modifiedAt
            }
        }

        val account2Update = Account(UUID.randomUUID().toString(), Instant.now(), Instant.now(), AccountStatus.Blocked)
        val customer2Update = Customer(newCustomer!!.customerUUID, Instant.now(), Instant.now(), CustomerStatus.Blocked, "Freddy", "Kruger", Language.de, account2Update)

        `when`("I call PUT /api/v1/admin/customers/{customerUUID} with updated fields") {
            val (updatedCustomer, status) = updateCustomer(testEngine, customer2Update.customerUUID, customer2Update)

            then("the response status should be OK") {
                status shouldBe HttpStatusCode.OK
            }

            then("customerUUID, createdAt and the account should not have changed") {
                newCustomer.customerUUID shouldBe updatedCustomer!!.customerUUID
                newCustomer.createdAt shouldBe updatedCustomer.createdAt
                newCustomer.account shouldBe updatedCustomer.account
            }

            then("the modified timestamp should have changed") {
                newCustomer.modifiedAt shouldBeLessThan updatedCustomer!!.modifiedAt
                newCustomer.createdAt shouldBeLessThan updatedCustomer.modifiedAt
            }

            then("first name, last name, language and status should have changed") {
                customer2Update.status shouldBe updatedCustomer!!.status
                customer2Update.firstName shouldBe updatedCustomer.firstName
                customer2Update.lastName shouldBe updatedCustomer.lastName
                customer2Update.language shouldBe updatedCustomer.language
            }
        }

        `when`("I call PUT /api/v1/admin/customers/{customerUUID} with an invalid uuid") {
            val (_, status) = updateCustomer(testEngine, "123456", customer2Update)

            then("the response status should be HTTP 400 BadRequest") {
                status shouldBe HttpStatusCode.BadRequest
            }
        }

        `when`("I call PUT /api/v1/admin/customers/{customerUUID} with a non existing uuid") {
            val (_, status) = updateCustomer(testEngine, "00000000-0000-0000-0000-000000000000", customer2Update)

            then("the response status should be HTTP 404 NotFound") {
                status shouldBe HttpStatusCode.NotFound
            }
        }
    }
} )

fun updateCustomer(testEngine: TestApplicationEngine, customerUUID: String, customer: Customer): Pair<Customer?, HttpStatusCode> {
    val call = testEngine.handleRequest(HttpMethod.Put, "/api/v1/admin/customers/$customerUUID") {
        setBody(gson.toJson(customer))
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }

    val result = runCatching {
        gson.fromJson(call.response.content.toString(), Customer::class.java)
    }
    return Pair(result.component1(), call.response.status() ?: HttpStatusCode.InternalServerError)
}