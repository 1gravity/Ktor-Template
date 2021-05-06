package com.onegravity.accountservice.persistence.model.customer

import com.onegravity.accountservice.persistence.model.Dao
import com.onegravity.accountservice.persistence.model.account.AccountDao
import com.onegravity.accountservice.route.model.customer.CreateCustomer
import com.onegravity.accountservice.route.model.customer.ResponseCustomer
import com.onegravity.accountservice.route.model.customer.UpdateCustomer
import com.onegravity.accountservice.util.NotFoundException
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import java.time.Instant
import java.util.*

class CustomerDao(private val accountDao: AccountDao, private val database: Database) :
    Dao<Customer, ResponseCustomer, CreateCustomer, UpdateCustomer> {

    override fun getAll() = database
        .sequenceOf(Customers)
        .toList()
        .map { toObject(it) }

    override fun get(uuid: String) = database.sequenceOf(Customers)
        .firstOrNull { it.customerUUID eq uuid }
        ?.let { toObject(it) }
        ?: throw NotFoundException("Customer with uuid $uuid not found")

    override fun insert(customer: CreateCustomer): ResponseCustomer {
        val newCustomer = Customer {
            val now = Instant.now()
            customerUUID = UUID.randomUUID().toString()
            createdAt = now
            modifiedAt = now
            firstName = customer.firstName
            lastName = customer.lastName
            language = customer.language
            status = customer.status
            account = accountDao.getAccount(customer.accountUUID)
        }
        database.sequenceOf(Customers).add(newCustomer)
        return get(newCustomer.customerUUID)
    }

    override fun update(customer: UpdateCustomer): ResponseCustomer {
        database.sequenceOf(Customers)
            .firstOrNull { it.customerUUID eq customer.customerUUID }
            ?.apply {
                modifiedAt = Instant.now()
                customer.firstName?.run { firstName = this }
                customer.lastName?.run { lastName = this }
                customer.status?.run { status = this }
                customer.language?.run { language = this }
                flushChanges()
            }
        return get(customer.customerUUID)
    }

    override fun delete(uuid: String): ResponseCustomer {
        database.sequenceOf(Customers)
            .firstOrNull { it.customerUUID eq uuid }
            ?.delete()
        return get(uuid)
    }

    override fun toObject(entity: Customer) = entity.run {
        ResponseCustomer(
            customerUUID,
            createdAt,
            modifiedAt,
            status,
            firstName,
            lastName,
            language,
            accountDao.toObject(account)
        )
    }

}
