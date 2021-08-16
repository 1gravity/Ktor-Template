package com.onegravity.accountservice.persistence.model.ktorm

import com.onegravity.accountservice.persistence.model.CustomerStatus
import com.onegravity.accountservice.persistence.model.Dao
import com.onegravity.accountservice.route.model.customer.CreateCustomer
import com.onegravity.accountservice.route.model.customer.ResponseCustomer
import com.onegravity.accountservice.route.model.customer.UpdateCustomer
import com.onegravity.util.NotFoundException
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import java.time.Instant
import java.util.*

class CustomerDao(private val accountDao: AccountDao, private val database: Database) :
    Dao<ResponseCustomer, CreateCustomer, UpdateCustomer> {

    override fun getAll() = database
        .sequenceOf(Customers)
        .toList()
        .map { toObject(it) }

    override fun get(uuid: String) = database.sequenceOf(Customers)
        .firstOrNull { Customers.customerUUID eq uuid }
        ?.let { toObject(it) }
        ?: throw NotFoundException("Customer with uuid $uuid not found")

    override fun insert(`object`: CreateCustomer): ResponseCustomer {
        val newCustomer = Customer {
            val now = Instant.now()
            customerUUID = UUID.randomUUID().toString()
            createdAt = now
            modifiedAt = now
            firstName = `object`.firstName
            lastName = `object`.lastName
            language = `object`.language
            status = `object`.status
            account = accountDao.getAccount(`object`.accountUUID)
        }
        database.sequenceOf(Customers).add(newCustomer)
        return get(newCustomer.customerUUID)
    }

    override fun update(uuid: String, `object`: UpdateCustomer): ResponseCustomer {
        database.sequenceOf(Customers)
            .firstOrNull { Customers.customerUUID eq uuid }
            ?.apply {
                `object`.firstName?.run { firstName = this }
                `object`.lastName?.run { lastName = this }
                `object`.status?.run { status = this }
                `object`.language?.run { language = this }
                modifiedAt = Instant.now()
                flushChanges()
            }
        return get(uuid)
    }

    override fun delete(uuid: String): ResponseCustomer {
        database.sequenceOf(Customers)
            .firstOrNull { Customers.customerUUID eq uuid }
            ?.apply {
                status = CustomerStatus.Deleted
                modifiedAt = Instant.now()
                flushChanges()
            }
        return get(uuid)
    }

    private fun toObject(entity: Customer) = entity.run {
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
