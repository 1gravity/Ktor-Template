@file:UseSerializers(KotlinxInstantSerializer::class)

package com.onegravity.accountservice.route.response

import com.onegravity.accountservice.controller.adapters.KotlinxInstantSerializer
import com.onegravity.accountservice.persistence.model.account.AccountStatus
import com.onegravity.accountservice.route.request.uuidPattern
import com.papsign.ktor.openapigen.annotations.Response
import com.papsign.ktor.openapigen.annotations.type.string.pattern.RegularExpression
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant
import com.onegravity.accountservice.persistence.model.account.Account as PersistentAccount

fun PersistentAccount.toResponse() = ResponseAccount(accountUUID, createdAt, modifiedAt, status)

@Serializable
@Response("Account object.", 200)
data class ResponseAccount(
    @RegularExpression(pattern = uuidPattern)
    val accountUUID: String,

    val createdAt: Instant,
    val modifiedAt: Instant,

    val status: AccountStatus
)