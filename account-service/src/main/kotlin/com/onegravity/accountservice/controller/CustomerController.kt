package com.onegravity.accountservice.controller

import com.onegravity.accountservice.route.response.Customer as ResponseCustomer
import com.onegravity.accountservice.route.request.Customer as RequestCustomer
import com.onegravity.accountservice.util.NotFoundException

interface CustomerController {

    suspend fun getCustomers(): List<ResponseCustomer>

    @Throws(NotFoundException::class)
    suspend fun getCustomer(customerUUID: String): ResponseCustomer

    @Throws(NotFoundException::class)
    suspend fun createCustomer(accountUUID: String, customer: RequestCustomer): ResponseCustomer

    @Throws(NotFoundException::class)
    suspend fun updateCustomer(customerUUID: String, customer: RequestCustomer): ResponseCustomer

    @Throws(NotFoundException::class)
    suspend fun deleteCustomer(customerUUID: String): ResponseCustomer

}
