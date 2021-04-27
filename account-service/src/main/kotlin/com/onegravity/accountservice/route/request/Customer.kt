package com.onegravity.accountservice.route.request

import com.onegravity.accountservice.persistence.model.customer.CustomerStatus
import com.onegravity.accountservice.persistence.model.customer.Language
import com.papsign.ktor.openapigen.annotations.type.string.length.Length
import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val status: CustomerStatus,

    @Length(2, 127)
    val firstName: String,

    @Length(2, 127)
    val lastName: String,

    val language: Language,
)