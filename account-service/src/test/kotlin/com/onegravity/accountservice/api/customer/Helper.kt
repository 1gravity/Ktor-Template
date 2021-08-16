package com.onegravity.accountservice.api.customer

import com.google.gson.Gson
import com.onegravity.accountservice.api.account.verifyAccount
import com.onegravity.accountservice.persistence.model.CustomerStatus
import com.onegravity.accountservice.persistence.model.Language
import com.onegravity.accountservice.route.misc.uuidPattern
import com.onegravity.accountservice.route.model.account.ResponseAccount
import com.onegravity.accountservice.route.model.customer.ResponseCustomer
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldHaveLengthBetween
import io.kotest.matchers.string.shouldMatch
import io.ktor.http.*
import io.ktor.server.testing.*
import java.time.Instant
import java.util.*
import kotlin.test.assertNotNull

fun createCustomer(testEngine: TestApplicationEngine, account: ResponseAccount, status: CustomerStatus, gson: Gson):
        Pair<ResponseCustomer, HttpStatusCode> {
    val uuid = UUID.randomUUID().toString()
    val now = Instant.now()
    val customer = TestCustomer(uuid, now, now, status, "Tom", "Sawyer", Language.en, account.accountUUID)
    return createCustomer(testEngine, customer, gson)
}

fun createCustomer(testEngine: TestApplicationEngine, customer: TestCustomer, gson: Gson):
        Pair<ResponseCustomer, HttpStatusCode> {
    val call = testEngine.handleRequest(HttpMethod.Post, "/api/v1/admin/customers") {
        setBody(gson.toJson(customer))
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }

    val result = runCatching {
        gson.fromJson(call.response.content.toString(), ResponseCustomer::class.java)
    }

    val responseCustomer = result.getOrNull()
    assertNotNull(responseCustomer)
    return Pair(responseCustomer, call.response.status() ?: HttpStatusCode.InternalServerError)
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
