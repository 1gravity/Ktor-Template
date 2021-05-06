@file:UseSerializers(KotlinxInstantSerializer::class)

package com.onegravity.accountservice.route.model.account

import com.onegravity.accountservice.controller.adapters.KotlinxInstantSerializer
import com.onegravity.accountservice.persistence.model.account.AccountStatus
import com.onegravity.accountservice.route.misc.uuidPattern
import com.papsign.ktor.openapigen.annotations.type.string.pattern.RegularExpression
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class UpdateAccount(
    @RegularExpression(pattern = uuidPattern)
    val accountUUID: String,

    val status: AccountStatus?
)