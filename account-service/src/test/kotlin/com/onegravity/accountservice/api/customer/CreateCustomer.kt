package com.onegravity.accountservice.api.customer

import com.onegravity.accountservice.api.account.createAccount
import com.onegravity.accountservice.persistence.model.account.AccountStatus
import com.onegravity.accountservice.persistence.model.customer.CustomerStatus
import com.onegravity.accountservice.persistence.model.customer.Language
import com.onegravity.accountservice.route.model.customer.ResponseCustomer
import com.onegravity.accountservice.util.testApplication
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.scopes.GivenScope
import io.kotest.matchers.shouldBe
import io.ktor.http.*
import io.ktor.server.testing.*
import java.time.Instant
import java.util.*

@Suppress("unused")
class CreateCustomer : BehaviorSpec( {
    testApplication { testEngine ->
        // create test account
        val (newAccount, _) = createAccount(testEngine, AccountStatus.Active)
        val accountUUID = newAccount!!.accountUUID

        val uuid = UUID.randomUUID().toString()
        val now = Instant.now()
        createAndTestCustomer(testEngine, TestCustomer(uuid, now, now, CustomerStatus.Active, "Tom", "Sawyer", Language.en, accountUUID))
        createAndTestCustomer(testEngine, TestCustomer(uuid, now, now, CustomerStatus.Deleted, "Kunigunde", "MacQuoid", Language.de, accountUUID))
        createAndTestCustomer(testEngine, TestCustomer(uuid, now, now, CustomerStatus.Blocked, "Abigail", "Thornton", Language.en, accountUUID))

        `when`("I call POST /api/v1/admin/customer with an invalid uuid") {
            val customer = TestCustomer(null, now, now, CustomerStatus.Blocked, "Abigail", "Thornton", Language.en, "123456")
            val (_, status) = createCustomer(testEngine, customer)

            then("the response status should be HTTP 400 BadRequest") {
                status shouldBe HttpStatusCode.BadRequest
            }
        }

        `when`("I call POST /api/v1/admin/customer with a non existing uuid") {
            val customer = TestCustomer(null, now, now, CustomerStatus.Blocked, "Abigail", "Thornton", Language.en, "00000000-0000-0000-0000-000000000000")
            val (_, status) = createCustomer(testEngine, customer)

            then("the response status should be HTTP 404 NotFound") {
                status shouldBe HttpStatusCode.NotFound
            }
        }
    }
} )

suspend fun GivenScope.createAndTestCustomer(testEngine: TestApplicationEngine, customer: TestCustomer): Pair<ResponseCustomer?, HttpStatusCode> {
    val (createdCustomer, status) = createCustomer(testEngine, customer)

    `when`("I call POST /api/v1/admin/customer (${Random().nextInt()})") {

        then("the response status should be OK") {
            status shouldBe HttpStatusCode.OK
        }

        then("the response body should be the created customer") {
            verifyCustomer(createdCustomer!!)
            createdCustomer.status shouldBe customer.status
            createdCustomer.firstName shouldBe customer.firstName
            createdCustomer.lastName shouldBe customer.lastName
            createdCustomer.language shouldBe customer.language
            createdCustomer.createdAt shouldBe createdCustomer.modifiedAt
        }
    }

    return Pair(createdCustomer, status)
}
