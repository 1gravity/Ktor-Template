package com.onegravity.accountservice.route.misc

import com.onegravity.accountservice.persistence.model.account.AccountStatus
import com.onegravity.accountservice.persistence.model.customer.CustomerStatus
import com.onegravity.accountservice.persistence.model.customer.Language
import com.onegravity.accountservice.route.model.account.CreateAccount
import com.onegravity.accountservice.route.model.account.ResponseAccount
import com.onegravity.accountservice.route.model.account.UpdateAccount
import com.onegravity.accountservice.route.model.customer.CreateCustomer
import com.onegravity.accountservice.route.model.customer.ResponseCustomer
import com.onegravity.accountservice.route.model.customer.UpdateCustomer
import java.time.Instant
import java.util.*

/* Requests */

val accountCreateExample = CreateAccount(status = AccountStatus.Active)

val accountUpdateExample = UpdateAccount(accountUUID = UUID.randomUUID().toString(), status = AccountStatus.Active)

val customerCreateExample = CreateCustomer(
    status = CustomerStatus.Active,
    firstName = "Peter",
    lastName = "Parker",
    language = Language.en,
    accountUUID = UUID.randomUUID().toString()
)

val customerUpdateExample = UpdateCustomer(
    customerUUID = UUID.randomUUID().toString(),
    status = CustomerStatus.Active,
    firstName = "Peter",
    lastName = "Parker",
    language = Language.en
)

/* Responses */

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