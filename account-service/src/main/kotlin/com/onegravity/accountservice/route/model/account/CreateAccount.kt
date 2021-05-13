@file:UseSerializers(KotlinxInstantSerializer::class)

package com.onegravity.accountservice.route.model.account

import com.onegravity.accountservice.persistence.model.AccountStatus
import com.onegravity.adapter.KotlinxInstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class CreateAccount(
    val status: AccountStatus
)