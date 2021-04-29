package com.onegravity.accountservice.route

import com.onegravity.accountservice.controller.AccountController
import com.onegravity.accountservice.route.request.AccountUUIDParam
import com.onegravity.accountservice.route.request.accountExampleRequest
import com.onegravity.accountservice.route.response.accountExampleResponse
import com.onegravity.accountservice.route.response.deletedAccountExampleResponse
import com.onegravity.accountservice.util.getKoinInstance
import com.papsign.ktor.openapigen.route.info
import com.papsign.ktor.openapigen.route.path.normal.*
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import com.papsign.ktor.openapigen.route.status
import com.papsign.ktor.openapigen.route.tag
import io.ktor.http.*
import com.onegravity.accountservice.route.request.Account as RequestAccount
import com.onegravity.accountservice.route.response.Account as ResponseAccount

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
                status(HttpStatusCode.OK),
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
                status(HttpStatusCode.OK),
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
            post<Unit, ResponseAccount, RequestAccount>(
                info(
                    summary = "Create an account.",
                    description = "Create a new account"
                ),
                status(HttpStatusCode.OK),
                exceptions = listOf(badRequest),
                exampleRequest = accountExampleRequest,
                exampleResponse = accountExampleResponse
            ) { _, account ->
                val newAccount = controller.createAccount(account)
                respond(newAccount)
            }

            /**
             * Update an account.
             */
            put<AccountUUIDParam, ResponseAccount, RequestAccount>(
                info(
                    summary = "Update an account.",
                    description = "Update an an account record"
                ),
                status(HttpStatusCode.OK),
                exceptions = listOf(badRequest, accountNotFound),
                exampleRequest = accountExampleRequest,
                exampleResponse = accountExampleResponse
            ) { params, account ->
                val accountUUID = params.accountUUID
                val updateAccount = controller.updateAccount(accountUUID, account)
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
                status(HttpStatusCode.OK),
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
