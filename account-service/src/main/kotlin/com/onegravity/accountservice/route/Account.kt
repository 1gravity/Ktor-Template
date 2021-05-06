package com.onegravity.accountservice.route

import com.onegravity.accountservice.controller.AccountController
import com.onegravity.accountservice.route.misc.*
import com.onegravity.accountservice.route.model.account.CreateAccount
import com.onegravity.accountservice.route.model.account.ResponseAccount
import com.onegravity.accountservice.route.model.account.UpdateAccount
import com.onegravity.accountservice.util.getKoinInstance
import com.papsign.ktor.openapigen.route.info
import com.papsign.ktor.openapigen.route.path.normal.*
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import com.papsign.ktor.openapigen.route.tag

fun NormalOpenAPIRoute.accountRouting() {

    val controller = getKoinInstance<AccountController>()

    tag(Tags.Account) {
        route("/api/v1/admin/accounts") {
            /**
             * Get a list of all accounts.
             */
            get<Unit, List<ResponseAccount>>(
                info(
                    summary = "Get all accounts.",
                    description = "Return a list of all accounts"
                ),
                example = listOf(accountExampleResponse),
            ) {
                respond(controller.getAccounts())
            }

            /**
             * Get one account.
             */
            get<AccountUUIDParam, ResponseAccount>(
                info(
                    summary = "Get an account.",
                    description = "Return an account by accountUUID"
                ),
                exceptions = listOf(badRequest, accountNotFound),
                example = accountExampleResponse,
            ) { params ->
                val accountUUID = params.accountUUID
                val account = controller.getAccount(accountUUID)
                respond(account)
            }

            /**
             * Create a new account.
             */
            post<Unit, ResponseAccount, CreateAccount>(
                info(
                    summary = "Create an account.",
                    description = "Create a new account"
                ),
                exceptions = listOf(badRequest),
                exampleRequest = accountCreateExample,
                exampleResponse = accountExampleResponse
            ) { _, account ->
                val newAccount = controller.createAccount(account)
                respond(newAccount)
            }

            /**
             * Update an account.
             */
            put<Unit, ResponseAccount, UpdateAccount>(
                info(
                    summary = "Update an account.",
                    description = "Update an account record"
                ),
                exceptions = listOf(badRequest, accountNotFound),
                exampleRequest = accountUpdateExample,
                exampleResponse = accountExampleResponse
            ) { _, account ->
                val updateAccount = controller.updateAccount(account)
                respond(updateAccount)
            }

            /**
             * Delete an account.
             */
            delete<AccountUUIDParam, ResponseAccount>(
                info(
                    summary = "Delete an account.",
                    description = "Delete an existing account record"
                ),
                exceptions = listOf(badRequest, accountNotFound),
                example = deletedAccountExampleResponse
            ) { params ->
                val accountUUID = params.accountUUID
                val account = controller.deleteAccount(accountUUID)
                respond(account)
            }
        }
    }
}
