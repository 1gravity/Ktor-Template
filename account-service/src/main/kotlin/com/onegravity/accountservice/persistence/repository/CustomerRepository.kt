package com.onegravity.accountservice.persistence.repository

import com.onegravity.accountservice.util.NotFoundException
import com.onegravity.accountservice.route.request.Customer as RequestCustomer
import com.onegravity.accountservice.persistence.model.customer.Customer as PersistentCustomer

interface CustomerRepository {

    fun getCustomers(): List<PersistentCustomer>

    @Throws(NotFoundException::class)
    fun getCustomer(customerUUID: String?): PersistentCustomer

    @Throws(NotFoundException::class)
    fun createCustomer(accountUUID: String?, customer: RequestCustomer): PersistentCustomer

    @Throws(NotFoundException::class)
    fun updateCustomer(customerUUID: String?, customer: RequestCustomer): PersistentCustomer

    @Throws(NotFoundException::class)
    fun deleteCustomer(customerUUID: String?): PersistentCustomer

}
