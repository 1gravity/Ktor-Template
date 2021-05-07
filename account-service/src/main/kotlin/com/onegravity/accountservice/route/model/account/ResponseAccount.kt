@file:UseSerializers(KotlinxInstantSerializer::class)

package com.onegravity.accountservice.route.model.account

import com.onegravity.accountservice.controller.adapters.KotlinxInstantSerializer
import com.onegravity.accountservice.persistence.model.AccountStatus
import com.onegravity.accountservice.route.misc.uuidPattern
import com.papsign.ktor.openapigen.annotations.Response
import com.papsign.ktor.openapigen.annotations.type.string.pattern.RegularExpression
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

@Serializable
@Response("Account object.", 200)
data class ResponseAccount(
    @RegularExpression(pattern = uuidPattern)
    val accountUUID: String,

    val createdAt: Instant,
    val modifiedAt: Instant,

    val status: AccountStatus
)