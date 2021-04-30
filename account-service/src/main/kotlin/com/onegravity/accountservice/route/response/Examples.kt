package com.onegravity.accountservice.route.response

import com.onegravity.accountservice.persistence.model.account.AccountStatus
import com.onegravity.accountservice.persistence.model.customer.CustomerStatus
import com.onegravity.accountservice.persistence.model.customer.Language
import java.time.Instant

val accountExampleResponse = ResponseAccount(
    "990a3c80-2dbc-446b-9e97-b784c84d20ea",
    Instant.now(),
    Instant.now(),
    AccountStatus.Active
)

val deletedAccountExampleResponse = ResponseAccount(
    "990a3c80-2dbc-446b-9e97-b784c84d20ea",
    Instant.now(),
    Instant.now(),
    AccountStatus.Deleted
)

val customerExampleResponse = ResponseCustomer(
    "300e47ad-263c-4e9a-a0b7-26d1229eaba9",
    Instant.now(),
    Instant.now(),
    CustomerStatus.Active,
    "Peter",
    "Parker",
    Language.en,
    accountExampleResponse
)

val deletedCustomerExampleResponse = ResponseCustomer(
    "300e47ad-263c-4e9a-a0b7-26d1229eaba9",
    Instant.now(),
    Instant.now(),
    CustomerStatus.Deleted,
    "Peter",
    "Parker",
    Language.en,
    accountExampleResponse
)