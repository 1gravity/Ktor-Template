package com.onegravity.accountservice.api.customer

import com.google.gson.reflect.TypeToken
import com.onegravity.accountservice.api.account.createAccount
import com.onegravity.accountservice.persistence.model.account.AccountStatus
import com.onegravity.accountservice.persistence.model.customer.CustomerStatus
import com.onegravity.accountservice.route.response.ResponseCustomer as Customer
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
        // create test account
        val (newAccount, _) = createAccount(testEngine, AccountStatus.Active)

        // create 10 test customers
        repeat(10) {
            createCustomer(testEngine, newAccount!!, CustomerStatus.Active)
        }

        `when`("I call GET /api/v1/admin/customers") {
            val call = testEngine.handleRequest(HttpMethod.Get, "/api/v1/admin/customers")

            then("the response status should be OK") {
                call.response.status() shouldBe HttpStatusCode.OK
            }

            then("the response body should contain a list of customers") {
                val json = call.response.content

                val collectionType = object : TypeToken<Collection<Customer>>() {}.type
                val customers = gson.fromJson<List<Customer>>(json.toString(), collectionType)

                customers.isEmpty() shouldNotBe true

                customers.forEach { verifyCustomer(it) }
            }
        }
    }
} )
