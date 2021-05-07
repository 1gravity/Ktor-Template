package com.onegravity.accountservice.api.account

import com.github.michaelbull.result.runCatching
import com.onegravity.accountservice.persistence.model.AccountStatus
import com.onegravity.accountservice.route.misc.uuidPattern
import com.onegravity.accountservice.route.model.account.ResponseAccount
import com.onegravity.accountservice.util.gson
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldMatch
import io.ktor.http.*
import io.ktor.server.testing.*
import java.time.Instant
import java.util.*
import kotlin.test.assertNotNull

fun createAccount(testEngine: TestApplicationEngine, status: AccountStatus): Pair<ResponseAccount, HttpStatusCode> {
    val uuid = UUID.randomUUID().toString()
    val now = Instant.now()
    val account = ResponseAccount(uuid, now, now, status)

    val call = testEngine.handleRequest(HttpMethod.Post, "/api/v1/admin/accounts") {
        setBody(gson.toJson(account))
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }

    val result = runCatching {
        gson.fromJson(call.response.content.toString(), ResponseAccount::class.java)
    }
    val responseAccount = result.component1()
    assertNotNull(responseAccount)
    return Pair(responseAccount, call.response.status() ?: HttpStatusCode.InternalServerError)
}

fun verifyAccount(account: ResponseAccount) {
    with (account) {
        accountUUID shouldNotBe null
        createdAt shouldNotBe null
        modifiedAt shouldNotBe null
        status shouldNotBe null

        accountUUID shouldMatch uuidPattern.toRegex()
        assert(createdAt <= modifiedAt)
    }
}
