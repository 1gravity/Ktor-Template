@file:UseSerializers(KotlinxInstantSerializer::class)

package com.onegravity.accountservice.route.model.customer

import com.onegravity.accountservice.controller.adapters.KotlinxInstantSerializer
import com.onegravity.accountservice.persistence.model.CustomerStatus
import com.onegravity.accountservice.persistence.model.Language
import com.onegravity.accountservice.route.misc.uuidPattern
import com.papsign.ktor.openapigen.annotations.type.string.length.Length
import com.papsign.ktor.openapigen.annotations.type.string.pattern.RegularExpression
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class CreateCustomer(
    val status: CustomerStatus,

    @Length(2, 127)
    val firstName: String,

    @Length(2, 127)
    val lastName: String,

    val language: Language,

    @RegularExpression(pattern = uuidPattern)
    val accountUUID: String,
)
