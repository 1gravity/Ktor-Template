@file:UseSerializers(KotlinxInstantSerializer::class)

package com.onegravity.accountservice.route.model.account

import com.onegravity.accountservice.persistence.model.AccountStatus
import com.onegravity.accountservice.route.misc.uuidPattern
import com.onegravity.adapter.KotlinxInstantSerializer
import com.papsign.ktor.openapigen.annotations.type.string.pattern.RegularExpression
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class UpdateAccount(
    @RegularExpression(pattern = uuidPattern)
    val accountUUID: String,

    val status: AccountStatus?
)