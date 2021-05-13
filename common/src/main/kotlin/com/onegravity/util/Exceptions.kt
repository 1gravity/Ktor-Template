package com.onegravity.util

import com.papsign.ktor.openapigen.annotations.type.common.ConstraintViolation

/**
 * Validation of input parameters failed.
 */
class ValidationException(msg: String) : ConstraintViolation(msg)

/**
 * A resource could not be found.
 */
class NotFoundException(msg: String) : Exception(msg)

/**
 * User doesn't have access to a resource.
 */
class IllegalAccessException(msg: String) : SecurityException(msg)

/**
 * Operation failed for some reason e.g. because the db or some external service wasn't available.
 */
class ServiceUnavailable(msg: String) : Exception(msg)
