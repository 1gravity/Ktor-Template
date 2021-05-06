package com.onegravity.accountservice.api.customer

import com.onegravity.accountservice.persistence.model.customer.CustomerStatus
import com.onegravity.accountservice.persistence.model.customer.Language
import java.time.Instant

data class TestCustomer(
    val customerUUID: String? = null,

    val createdAt: Instant,
    val modifiedAt: Instant,

    val status: CustomerStatus,

    val firstName: String,
    val lastName: String,

    val language: Language,

    val accountUUID: String? = null,
)
