@file:UseSerializers(KotlinxInstantSerializer::class)

package com.onegravity.accountservice.route.model.account

import com.onegravity.accountservice.controller.adapters.KotlinxInstantSerializer
import com.onegravity.accountservice.persistence.model.AccountStatus
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class CreateAccount(
    val status: AccountStatus
)