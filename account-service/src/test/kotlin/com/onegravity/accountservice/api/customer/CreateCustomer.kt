package com.onegravity.accountservice.api.customer

import com.google.gson.Gson
import com.onegravity.accountservice.api.account.createAccount
import com.onegravity.accountservice.persistence.model.AccountStatus
import com.onegravity.accountservice.persistence.model.CustomerStatus
import com.onegravity.accountservice.persistence.model.Language
import com.onegravity.accountservice.route.model.customer.ResponseCustomer
import com.onegravity.accountservice.util.testApps
import com.onegravity.util.getKoinInstance
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.scopes.BehaviorSpecGivenContainerContext as Context
import io.kotest.matchers.shouldBe
import io.ktor.http.*
import io.ktor.server.testing.*
import java.time.Instant
import java.util.*

@Suppress("unused")
class CreateCustomer : BehaviorSpec( {
    testApps(this) { testEngine, prefix ->
        val gson = getKoinInstance<Gson>()

        // create test account
        val (newAccount, _) = createAccount(testEngine, AccountStatus.Active, gson)
        val accountUUID = newAccount.accountUUID

        val uuid = UUID.randomUUID().toString()
        val now = Instant.now()
        createAndTestCustomer(testEngine, TestCustomer(uuid, now, now, CustomerStatus.Active, "Tom", "Sawyer", Language.en, accountUUID), gson)
        createAndTestCustomer(testEngine, TestCustomer(uuid, now, now, CustomerStatus.Deleted, "Kunigunde", "MacQuoid", Language.de, accountUUID), gson)
        createAndTestCustomer(testEngine, TestCustomer(uuid, now, now, CustomerStatus.Blocked, "Abigail", "Thornton", Language.en, accountUUID), gson)

        `when`("$prefix - I call POST /api/v1/admin/customer with an invalid uuid") {
            val customer = TestCustomer(null, now, now, CustomerStatus.Blocked, "Abigail", "Thornton", Language.en, "123456")
            val (_, status) = createCustomer(testEngine, customer, gson)

            then("the response status should be HTTP 400 BadRequest") {
                status shouldBe HttpStatusCode.BadRequest
            }
        }

        `when`("$prefix - I call POST /api/v1/admin/customer with a non existing uuid") {
            val customer = TestCustomer(null, now, now, CustomerStatus.Blocked, "Abigail", "Thornton", Language.en, "00000000-0000-0000-0000-000000000000")
            val (_, status) = createCustomer(testEngine, customer, gson)

            then("the response status should be HTTP 404 NotFound") {
                status shouldBe HttpStatusCode.NotFound
            }
        }

        `when`("$prefix - I call POST /api/v1/admin/customer with an invalid customer status") {
            val call = testEngine.handleRequest(HttpMethod.Post, "/api/v1/admin/customers") {
                setBody("{" +
                        "    \"accountUUID\": \"$accountUUID\",\n" +
                        "    \"status\": \"NotActive\"\n" +
                        "}"
                )
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }

            then("the response status should be HTTP 400 BadRequest") {
                call.response.status() shouldBe HttpStatusCode.BadRequest
            }
        }

        `when`("$prefix - I call POST /api/v1/admin/customer with an invalid language") {
            val call = testEngine.handleRequest(HttpMethod.Post, "/api/v1/admin/customers") {
                setBody("{" +
                        "    \"accountUUID\": \"$accountUUID\",\n" +
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

suspend fun Context.createAndTestCustomer(testEngine: TestApplicationEngine, customer: TestCustomer, gson: Gson):
        Pair<ResponseCustomer?, HttpStatusCode> {
    val (createdCustomer, status) = createCustomer(testEngine, customer, gson)

    `when`("I call POST /api/v1/admin/customer (${Random().nextInt()})") {

        then("the response status should be OK") {
            status shouldBe HttpStatusCode.OK
        }

        then("the response body should be the created customer") {
            verifyCustomer(createdCustomer)
            createdCustomer.status shouldBe customer.status
            createdCustomer.firstName shouldBe customer.firstName
            createdCustomer.lastName shouldBe customer.lastName
            createdCustomer.language shouldBe customer.language
            createdCustomer.createdAt shouldBe createdCustomer.modifiedAt
        }
    }

    return Pair(createdCustomer, status)
}
