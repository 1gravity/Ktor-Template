@file:UseSerializers(KotlinxInstantSerializer::class)

package com.onegravity.accountservice.route.response

import com.onegravity.accountservice.controller.adapters.KotlinxInstantSerializer
import com.onegravity.accountservice.persistence.model.customer.CustomerStatus
import com.onegravity.accountservice.persistence.model.customer.Language
import com.onegravity.accountservice.route.request.uuidPattern
import com.papsign.ktor.openapigen.annotations.Response
import com.papsign.ktor.openapigen.annotations.type.string.length.Length
import com.papsign.ktor.openapigen.annotations.type.string.pattern.RegularExpression
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant
import com.onegravity.accountservice.persistence.model.customer.Customer as PersistentCustomer

fun PersistentCustomer.toResponse() =
    ResponseCustomer(customerUUID, createdAt, modifiedAt, status, firstName, lastName, language, account.toResponse())

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
