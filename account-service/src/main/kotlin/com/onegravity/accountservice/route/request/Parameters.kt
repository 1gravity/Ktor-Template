package com.onegravity.accountservice.route.request

import com.papsign.ktor.openapigen.annotations.Path
import com.papsign.ktor.openapigen.annotations.parameters.PathParam
import com.papsign.ktor.openapigen.annotations.type.string.pattern.RegularExpression

const val uuidPattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"

@Path("{customerUUID}")
data class CustomerUUIDParam(
    @PathParam("UUID of the customer.")
    @RegularExpression(pattern = uuidPattern, "customerUUID doesn't match \"$uuidPattern\"")
    val customerUUID: String
)

@Path("{accountUUID}")
data class AccountUUIDParam(
    @PathParam("UUID of the account.")
    @RegularExpression(pattern = uuidPattern, "accountUUID doesn't match \"$uuidPattern\"")
    val accountUUID: String
)
