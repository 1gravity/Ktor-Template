@file:Suppress("unused")

package com.onegravity.accountservice.route

import com.onegravity.accountservice.controller.CustomerController
import com.onegravity.accountservice.route.request.AccountUUIDParam
import com.onegravity.accountservice.route.request.CustomerUUIDParam
import com.onegravity.accountservice.route.request.customerExampleRequest
import com.onegravity.accountservice.route.response.customerExampleResponse
import com.onegravity.accountservice.route.response.deletedCustomerExampleResponse
import com.onegravity.accountservice.util.getKoinInstance
import com.papsign.ktor.openapigen.route.info
import com.papsign.ktor.openapigen.route.path.normal.*
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import com.papsign.ktor.openapigen.route.tag
import com.onegravity.accountservice.route.request.RequestCustomer
import com.onegravity.accountservice.route.response.ResponseCustomer

fun NormalOpenAPIRoute.customerRouting() {

    val controller = getKoinInstance<CustomerController>()

    tag(Tags.Customer) {
        route("/api/v1/admin/customers") {
            /**
             * Get a list of all customers.
             */
            get<Unit, List<ResponseCustomer>>(
                info(
                    summary = "Get all customers.",
                    description = "Return a list of all customers"
                ),
                example = listOf(customerExampleResponse, customerExampleResponse),
            ) {
                respond(controller.getCustomers())
            }

            /**
             * Get one customer.
             */
            get<CustomerUUIDParam, ResponseCustomer>(
                info(
                    summary = "Get a customer.",
                    description = "Return a customer by customerUUID"
                ),
                exceptions = listOf(badRequest, customerNotFound),
                example = customerExampleResponse,
            ) { params ->
                val customerUUID = params.customerUUID
                val customer = controller.getCustomer(customerUUID)
                respond(customer)
            }

            /**
             * Create a new customer.
             */
            post<AccountUUIDParam, ResponseCustomer, RequestCustomer>(
                info(
                    summary = "Create a customer.",
                    description = "Create a new customer record"
                ),
                exceptions = listOf(badRequest, accountNotFound),
                exampleRequest = customerExampleRequest,
                exampleResponse = customerExampleResponse
            ) { params, customer ->
                val accountUUID = params.accountUUID
                val newCustomer = controller.createCustomer(accountUUID, customer)
                respond(newCustomer)
            }

            /**
             * Update a customer.
             */
            put<CustomerUUIDParam, ResponseCustomer, RequestCustomer>(
                info(
                    summary = "Update a customer.",
                    description = "Update an existing customer record"
                ),
                exceptions = listOf(badRequest, customerNotFound),
                exampleRequest = customerExampleRequest,
                exampleResponse = customerExampleResponse
            ) { params, customer ->
                val customerUUID = params.customerUUID
                val updateCustomer = controller.updateCustomer(customerUUID, customer)
                respond(updateCustomer)
            }

            /**
             * Delete a customer.
             */
            delete<CustomerUUIDParam, ResponseCustomer>(
                info(
                    summary = "Delete a customer.",
                    description = "Delete an existing customer record"
                ),
                exceptions = listOf(badRequest, customerNotFound),
                example = deletedCustomerExampleResponse
            ) { params ->
                val customerUUID = params.customerUUID
                val customer = controller.deleteCustomer(customerUUID)
                respond(customer)
            }
        }
    }
}
