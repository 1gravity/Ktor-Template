package com.onegravity.accountservice.route.request

import com.onegravity.accountservice.persistence.model.account.AccountStatus
import kotlinx.serialization.Serializable

@Serializable
data class RequestAccount(
    val status: AccountStatus
)
