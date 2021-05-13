package com.onegravity.accountservice.api.customer

import com.google.gson.Gson
import com.onegravity.accountservice.api.account.createAccount
import com.onegravity.accountservice.persistence.model.AccountStatus
import com.onegravity.accountservice.persistence.model.CustomerStatus
import com.onegravity.accountservice.route.model.customer.ResponseCustomer
import com.onegravity.accountservice.util.testApps
import com.onegravity.util.getKoinInstance
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.*
import io.ktor.server.testing.*

@Suppress("unused")
class GetCustomer : BehaviorSpec( {
    testApps(this) { testEngine, prefix ->
        val gson = getKoinInstance<Gson>()

        // create test account
        val (newAccount, _) = createAccount(testEngine, AccountStatus.Active, gson)

        // create test customer
        val (newCustomer, _) = createCustomer(testEngine, newAccount, CustomerStatus.Active, gson)

        `when`("$prefix - I call GET /api/v1/admin/customers/{customerUUID}") {
            val call = testEngine.handleRequest(HttpMethod.Get, "/api/v1/admin/customers/${newCustomer.customerUUID}")

            then("the response status should be OK") {
                call.response.status() shouldBe HttpStatusCode.OK
            }

            then("the response body should contain the newly created customer") {
                val customer = gson.fromJson(call.response.content.toString(), ResponseCustomer::class.java)
                customer shouldBe newCustomer
            }
        }

        `when`("$prefix - I call GET /api/v1/admin/customers/{customerUUID} with an invalid uuid") {
            val call = testEngine.handleRequest(HttpMethod.Get, "/api/v1/admin/customers/123456")

            then("the response status should be HTTP 400 BadRequest") {
                call.response.status() shouldBe HttpStatusCode.BadRequest
            }
        }

        `when`("$prefix - I call GET /api/v1/admin/customers/{customerUUID} with a non existing uuid") {
            val call = testEngine.handleRequest(HttpMethod.Get, "/api/v1/admin/customers/00000000-0000-0000-0000-000000000000")

            then("the response status should be HTTP 404 NotFound") {
                call.response.status() shouldBe HttpStatusCode.NotFound
            }
        }
    }
} )
