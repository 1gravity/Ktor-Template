package com.onegravity.accountservice.api.account

import com.github.michaelbull.result.runCatching
import com.onegravity.accountservice.persistence.model.account.AccountStatus
import com.onegravity.accountservice.route.misc.uuidPattern
import com.onegravity.accountservice.route.model.account.ResponseAccount as Account
import com.onegravity.accountservice.util.gson
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldMatch
import io.ktor.http.*
import io.ktor.server.testing.*
import java.time.Instant
import java.util.*

fun createAccount(testEngine: TestApplicationEngine, status: AccountStatus): Pair<Account?, HttpStatusCode> {
    val uuid = UUID.randomUUID().toString()
    val now = Instant.now()
    val account = Account(uuid, now, now, status)

    val call = testEngine.handleRequest(HttpMethod.Post, "/api/v1/admin/accounts") {
        setBody(gson.toJson(account))
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }

    val result = runCatching {
        gson.fromJson(call.response.content.toString(), Account::class.java)
    }
    return Pair(result.component1(), call.response.status() ?: HttpStatusCode.InternalServerError)
}

fun verifyAccount(account: Account) {
    with (account) {
        accountUUID shouldNotBe null
        createdAt shouldNotBe null
        modifiedAt shouldNotBe null
        status shouldNotBe null

        accountUUID shouldMatch uuidPattern.toRegex()
        assert(createdAt <= modifiedAt)
    }
}
