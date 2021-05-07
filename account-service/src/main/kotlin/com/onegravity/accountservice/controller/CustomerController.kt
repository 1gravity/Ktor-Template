package com.onegravity.accountservice.controller

import com.onegravity.accountservice.route.model.customer.CreateCustomer
import com.onegravity.accountservice.route.model.customer.ResponseCustomer
import com.onegravity.accountservice.route.model.customer.UpdateCustomer
import com.onegravity.accountservice.util.NotFoundException

interface CustomerController {

    suspend fun getCustomers(): List<ResponseCustomer>

    @Throws(NotFoundException::class)
    suspend fun getCustomer(customerUUID: String): ResponseCustomer

    @Throws(NotFoundException::class)
    suspend fun createCustomer(customer: CreateCustomer): ResponseCustomer

    @Throws(NotFoundException::class)
    suspend fun updateCustomer(customer: UpdateCustomer): ResponseCustomer

    @Throws(NotFoundException::class)
    suspend fun deleteCustomer(customerUUID: String): ResponseCustomer

}
