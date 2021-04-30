package com.onegravity.accountservice.api.customer

import com.onegravity.accountservice.api.account.createAccount
import com.onegravity.accountservice.persistence.model.account.AccountStatus
import com.onegravity.accountservice.persistence.model.customer.CustomerStatus
import com.onegravity.accountservice.route.response.ResponseCustomer as Customer
import com.onegravity.accountservice.util.gson
import com.onegravity.accountservice.util.testApplication
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.*
import io.ktor.server.testing.*

@Suppress("unused")
class GetCustomer : BehaviorSpec( {
    testApplication { testEngine ->
        // create test account
        val (newAccount, _) = createAccount(testEngine, AccountStatus.Active)

        // create test customer
        val (newCustomer, _) = createCustomer(testEngine, newAccount!!, CustomerStatus.Active)

        `when`("I call GET /api/v1/admin/customers/{customerUUID}") {
            val call = testEngine.handleRequest(HttpMethod.Get, "/api/v1/admin/customers/${newCustomer!!.customerUUID}")

            then("the response status should be OK") {
                call.response.status() shouldBe HttpStatusCode.OK
            }

            then("the response body should contain the newly created customer") {
                val customer = gson.fromJson(call.response.content.toString(), Customer::class.java)
                customer shouldBe newCustomer
            }
        }

        `when`("I call GET /api/v1/admin/customers/{customerUUID} with an invalid uuid") {
            val call = testEngine.handleRequest(HttpMethod.Get, "/api/v1/admin/customers/123456")

            then("the response status should be HTTP 400 BadRequest") {
                call.response.status() shouldBe HttpStatusCode.BadRequest
            }
        }

        `when`("I call GET /api/v1/admin/customers/{customerUUID} with a non existing uuid") {
            val call = testEngine.handleRequest(HttpMethod.Get, "/api/v1/admin/customers/00000000-0000-0000-0000-000000000000")

            then("the response status should be HTTP 404 NotFound") {
                call.response.status() shouldBe HttpStatusCode.NotFound
            }
        }
    }
} )
