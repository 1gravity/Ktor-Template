package com.onegravity.accountservice.api.customer

import com.google.gson.Gson
import com.onegravity.accountservice.api.account.createAccount
import com.onegravity.accountservice.persistence.model.AccountStatus
import com.onegravity.accountservice.persistence.model.CustomerStatus
import com.onegravity.accountservice.route.model.customer.ResponseCustomer
import com.onegravity.accountservice.util.testApps
import com.onegravity.util.getKoinInstance
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.ktor.http.*
import io.ktor.server.testing.*

@Suppress("unused")
class DeleteCustomer : BehaviorSpec( {
    testApps(this) { testEngine, prefix ->
        val gson = getKoinInstance<Gson>()

        // create test account
        val (newAccount, _) = createAccount(testEngine, AccountStatus.Active, gson)

        // create test customer
        val (newCustomer, _) = createCustomer(testEngine, newAccount, CustomerStatus.Active, gson)

        `when`("$prefix - I call DELETE /api/v1/admin/customers/{customerUUID}") {
            val call = testEngine.handleRequest(HttpMethod.Delete, "/api/v1/admin/customers/${newCustomer.customerUUID}")
            val updatedCustomer = gson.fromJson(call.response.content.toString(), ResponseCustomer::class.java)

            then("the response status should be OK") {
                call.response.status() shouldBe HttpStatusCode.OK
            }

            then("customerUUID and createdAt should not have changed") {
                newCustomer.customerUUID shouldBe updatedCustomer.customerUUID
                newCustomer.createdAt shouldBe updatedCustomer.createdAt
            }

            then("the modified timestamp should have changed") {
                newCustomer.modifiedAt shouldBeLessThan updatedCustomer.modifiedAt
                updatedCustomer.createdAt shouldBeLessThan updatedCustomer.modifiedAt
            }

            then("the status should be Deleted") {
                updatedCustomer.status shouldBe CustomerStatus.Deleted
            }
        }

        `when`("$prefix - I call DELETE /api/v1/admin/customers/{customerUUID} with an invalid uuid") {
            val call = testEngine.handleRequest(HttpMethod.Delete, "/api/v1/admin/customers/123456")

            then("the response status should be HTTP 400 BadRequest") {
                call.response.status() shouldBe HttpStatusCode.BadRequest
            }
        }

        `when`("$prefix - I call PUT /api/v1/admin/customers/{customerUUID} with a non existing uuid") {
            val call = testEngine.handleRequest(HttpMethod.Delete, "/api/v1/admin/customers/00000000-0000-0000-0000-000000000000")

            then("the response status should be HTTP 404 NotFound") {
                call.response.status() shouldBe HttpStatusCode.NotFound
            }
        }
    }
} )
