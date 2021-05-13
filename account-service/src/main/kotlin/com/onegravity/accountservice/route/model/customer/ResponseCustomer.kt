@file:UseSerializers(KotlinxInstantSerializer::class)

package com.onegravity.accountservice.route.model.customer

import com.onegravity.accountservice.persistence.model.CustomerStatus
import com.onegravity.accountservice.persistence.model.Language
import com.onegravity.accountservice.route.misc.uuidPattern
import com.onegravity.accountservice.route.model.account.ResponseAccount
import com.onegravity.adapter.KotlinxInstantSerializer
import com.papsign.ktor.openapigen.annotations.Response
import com.papsign.ktor.openapigen.annotations.type.string.length.Length
import com.papsign.ktor.openapigen.annotations.type.string.pattern.RegularExpression
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

@Serializable
@Response("Customer object.", 200)
data class ResponseCustomer(
    @RegularExpression(pattern = uuidPattern)
    val customerUUID: String,

    val createdAt: Instant,
    val modifiedAt: Instant,

    val status: CustomerStatus,

    @Length(2, 127)
    val firstName: String,
    @Length(2, 127)
    val lastName: String,

    val language: Language,

    val account: ResponseAccount,
)
