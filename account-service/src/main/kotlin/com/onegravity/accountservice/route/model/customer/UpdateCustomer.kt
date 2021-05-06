@file:UseSerializers(KotlinxInstantSerializer::class)

package com.onegravity.accountservice.route.model.customer

import com.onegravity.accountservice.controller.adapters.KotlinxInstantSerializer
import com.onegravity.accountservice.persistence.model.customer.CustomerStatus
import com.onegravity.accountservice.persistence.model.customer.Language
import com.onegravity.accountservice.route.misc.uuidPattern
import com.papsign.ktor.openapigen.annotations.type.string.length.Length
import com.papsign.ktor.openapigen.annotations.type.string.pattern.RegularExpression
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class UpdateCustomer(
    @RegularExpression(pattern = uuidPattern)
    val customerUUID: String,

    val status: CustomerStatus?,

    @Length(2, 127)
    val firstName: String?,

    @Length(2, 127)
    val lastName: String?,

    val language: Language?,
)
