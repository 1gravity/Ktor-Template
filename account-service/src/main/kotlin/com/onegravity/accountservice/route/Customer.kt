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
import com.papsign.ktor.openapigen.route.status
import com.papsign.ktor.openapigen.route.tag
import io.ktor.http.*
import com.onegravity.accountservice.route.request.Customer as RequestCustomer
import com.onegravity.accountservice.route.response.Customer as ResponseCustomer

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
                status(HttpStatusCode.OK),
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
                status(HttpStatusCode.OK),
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
                status(HttpStatusCode.OK),
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
                status(HttpStatusCode.OK),
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
                status(HttpStatusCode.OK),
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

/**
 * Standard Ktor routing
 */
//fun Route.customerRouting() {
//    val controller = getKoinInstance<CustomerController>()
//
//    route("/api/v1/admin/customers") {
//        get {
//            call.respond(HttpStatusCode.OK, controller.getCustomers())
//        }
//
//        get("/{customerUUID}") {
//            val customerUUID = call.getUUID("customerUUID")
//            val customer = controller.getCustomer(customerUUID)
//            call.respond(HttpStatusCode.OK, customer)
//        }
//
//        post("/{accountUUID}") {
//            val accountUUID = call.getUUID("accountUUID")
//            val customer = call.receive<Customer>().validate()
//            val newCustomer = controller.createCustomer(accountUUID, customer)
//            call.respond(HttpStatusCode.NotModified, newCustomer)
//        }
//
//        put {
//            val customer = call.receive<Customer>().validate()
//            val updatedCustomer = controller.updateCustomer(customer)
//            call.respond(HttpStatusCode.OK, updatedCustomer)
//        }
//
//        delete("/{customerUUID}") {
//            val customerUUID = call.getUUID("customerUUID")
//            val customer = controller.deleteCustomer(customerUUID)
//            call.respond(HttpStatusCode.OK, customer)
//        }
//    }
//
//}
