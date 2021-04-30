@file:UseSerializers(KotlinxInstantSerializer::class)

package com.onegravity.accountservice.route.response

import com.onegravity.accountservice.controller.adapters.KotlinxInstantSerializer
import com.onegravity.accountservice.persistence.model.customer.CustomerStatus
import com.onegravity.accountservice.persistence.model.customer.Language
import com.papsign.ktor.openapigen.annotations.Response
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant
import com.onegravity.accountservice.persistence.model.customer.Customer as PersistentCustomer

fun PersistentCustomer.toResponse() =
    Customer(customerUUID, createdAt, modifiedAt, status, firstName, lastName, language, account.toResponse())

@Serializable
@Response("Customer object.", 200)
data class Customer(
    val customerUUID: String,

    val createdAt: Instant,
    val modifiedAt: Instant,

    val status: CustomerStatus,

    val firstName: String,
    val lastName: String,
    val language: Language,

    val account: Account,
)
