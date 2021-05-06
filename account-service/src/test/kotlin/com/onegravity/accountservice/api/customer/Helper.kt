package com.onegravity.accountservice.api.customer

import com.github.michaelbull.result.runCatching
import com.onegravity.accountservice.api.account.verifyAccount
import com.onegravity.accountservice.persistence.model.customer.CustomerStatus
import com.onegravity.accountservice.persistence.model.customer.Language
import com.onegravity.accountservice.route.misc.uuidPattern
import com.onegravity.accountservice.route.model.account.ResponseAccount
import com.onegravity.accountservice.route.model.customer.ResponseCustomer
import com.onegravity.accountservice.util.gson
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldHaveLengthBetween
import io.kotest.matchers.string.shouldMatch
import io.ktor.http.*
import io.ktor.server.testing.*
import java.time.Instant
import java.util.*

fun createCustomer(testEngine: TestApplicationEngine, account: ResponseAccount, status: CustomerStatus): Pair<ResponseCustomer?, HttpStatusCode> {
    val uuid = UUID.randomUUID().toString()
    val now = Instant.now()
    val customer = TestCustomer(uuid, now, now, status, "Tom", "Sawyer", Language.en, account.accountUUID)
    return createCustomer(testEngine, customer)
}

fun createCustomer(testEngine: TestApplicationEngine, customer: TestCustomer): Pair<ResponseCustomer?, HttpStatusCode> {
    val call = testEngine.handleRequest(HttpMethod.Post, "/api/v1/admin/customers") {
        setBody(gson.toJson(customer))
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }

    val result = runCatching {
        gson.fromJson(call.response.content.toString(), ResponseCustomer::class.java)
    }

    return Pair(result.component1(), call.response.status() ?: HttpStatusCode.InternalServerError)
}

fun verifyCustomer(customer: ResponseCustomer) {
    with (customer) {
        customerUUID shouldNotBe null
        createdAt shouldNotBe null
        modifiedAt shouldNotBe null
        status shouldNotBe null
        language shouldNotBe null
        account shouldNotBe null

        customerUUID shouldMatch uuidPattern.toRegex()
        firstName.shouldHaveLengthBetween(2, 127)
        lastName.shouldHaveLengthBetween(2, 127)
        assert(createdAt <= modifiedAt)

        verifyAccount(customer.account)
    }
}
