package com.onegravity.accountservice.route

import com.onegravity.accountservice.util.NotFoundException
import com.papsign.ktor.openapigen.APIException.APIExceptionBuilder.Companion.apiException
import com.papsign.ktor.openapigen.annotations.type.common.ConstraintViolation
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class ErrorMessage(val error: String)

val badRequest = apiException<ConstraintViolation, ErrorMessage> {
    status = HttpStatusCode.BadRequest.description("Invalid Request")
    example = ErrorMessage("parameter doesn't match \"^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\$\"")
    contentFn = { ErrorMessage(it.message ?: "Bad Request") }
}

val customerNotFound = apiException<NotFoundException, ErrorMessage> {
    status = HttpStatusCode.NotFound.description("Customer not found")
    example = ErrorMessage("Customer with uuid 300e47ad-263c-4e9a-a0b7-26d1229eaba8 not found")
    contentFn = { ErrorMessage(it.message ?: "Customer not found") }
}

val accountNotFound = apiException<NotFoundException, ErrorMessage> {
    status = HttpStatusCode.NotFound.description("Account not found")
    example = ErrorMessage("Account with uuid 990a3c80-2dbc-446b-9e97-b784c84d20ea not found")
    contentFn = { ErrorMessage(it.message ?: "Account not found") }
}
