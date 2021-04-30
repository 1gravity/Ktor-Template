package com.onegravity.accountservice.persistence.repository

import com.onegravity.accountservice.persistence.database.Database
import com.onegravity.accountservice.persistence.model.customer.CustomerStatus
import com.onegravity.accountservice.util.NotFoundException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.toList
import java.time.Instant
import java.util.*
import com.onegravity.accountservice.route.request.RequestCustomer
import com.onegravity.accountservice.persistence.model.customer.Customer as PersistentCustomer

class CustomerRepositoryImpl : CustomerRepository, KoinComponent {

    private val database by inject<Database>()

    private val accountRepo by inject<AccountRepository>()

    override fun getCustomers() = database.customers().toList()

    override fun getCustomer(customerUUID: String?) =
        customerUUID
            ?.run { database.customers().firstOrNull { it.customerUUID eq customerUUID } }
            ?: throw NotFoundException("Customer with uuid $customerUUID not found")

    override fun createCustomer(accountUUID: String?, customer: RequestCustomer): PersistentCustomer {
        val account = accountRepo.getAccount(accountUUID)
        val newCustomer = PersistentCustomer {
            val now = Instant.now()
            customerUUID = UUID.randomUUID().toString()
            createdAt = now
            modifiedAt = now
            firstName = customer.firstName
            lastName = customer.lastName
            language = customer.language
            status = customer.status
            this.account = account
        }
        database.customers().add(newCustomer)
        // if we return newCustomer / PersistentCustomer the time format will be different than what we store in the db
        return getCustomer(newCustomer.customerUUID)
    }

    override fun updateCustomer(customerUUID: String?, customer: RequestCustomer) =
        getCustomer(customerUUID)
            .also {
                it.modifiedAt = Instant.now()
                it.firstName = customer.firstName
                it.lastName = customer.lastName
                it.status = customer.status
                it.language = customer.language
                it.flushChanges()
            }

    override fun deleteCustomer(customerUUID: String?) =
        getCustomer(customerUUID)
            .also {
                it.modifiedAt = Instant.now()
                it.status = CustomerStatus.Deleted
                it.flushChanges()
            }

}
