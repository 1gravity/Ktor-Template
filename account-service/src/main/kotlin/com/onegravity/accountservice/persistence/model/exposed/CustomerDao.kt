package com.onegravity.accountservice.persistence.model.exposed

import com.onegravity.accountservice.persistence.model.CustomerStatus
import com.onegravity.accountservice.persistence.model.Dao
import com.onegravity.accountservice.route.model.customer.CreateCustomer
import com.onegravity.accountservice.route.model.customer.ResponseCustomer
import com.onegravity.accountservice.route.model.customer.UpdateCustomer
import com.onegravity.accountservice.util.NotFoundException
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.*

class CustomerDao(private val accountDao: AccountDao) :
    Dao<ResponseCustomer, CreateCustomer, UpdateCustomer> {

    override fun getAll() = transaction {
        Customer.all()
            .toList()
            .map { toObject(it) }
    }

    override fun get(uuid: String) = transaction {
        Customer
            .find { Customers.customerUUID eq uuid }
            .firstOrNull()
            ?.let { toObject(it) }
            ?: throw NotFoundException("Customer with uuid $uuid not found")
    }

    override fun insert(`object`: CreateCustomer): ResponseCustomer {
        return transaction {
            val newCustomerUUID = UUID.randomUUID().toString()
            Customer.new {
                val now = Instant.now()
                customerUUID = newCustomerUUID
                createdAt = now.toDateTime()
                modifiedAt = now.toDateTime()
                firstName = `object`.firstName
                lastName = `object`.lastName
                language = `object`.language
                status = `object`.status
                account = accountDao.getAccount(`object`.accountUUID)
            }
            get(newCustomerUUID)
        }
    }

    override fun update(`object`: UpdateCustomer): ResponseCustomer {
        return transaction {
            getCustomer(`object`.customerUUID).apply {
                `object`.firstName?.run { firstName = this }
                `object`.lastName?.run { lastName = this }
                `object`.status?.run { status = this }
                `object`.language?.run { language = this }
                modifiedAt = Instant.now().toDateTime()
            }

            get(`object`.customerUUID)
        }
    }

    override fun delete(uuid: String): ResponseCustomer {
        return transaction {
            getCustomer(uuid).apply {
                status = CustomerStatus.Deleted
                modifiedAt = Instant.now().toDateTime()
            }

            get(uuid)
        }
    }

    @Throws(NotFoundException::class)
    fun getCustomer(uuid: String) = Customer
        .find { Customers.customerUUID eq uuid }
        .firstOrNull()
        ?: throw NotFoundException("Customer with uuid $uuid not found")

    private fun toObject(entity: Customer) = entity.run {
        ResponseCustomer(
            customerUUID,
            createdAt.toJavaInstant(),
            modifiedAt.toJavaInstant(),
            status,
            firstName,
            lastName,
            language,
            accountDao.toObject(entity.account)
        )
    }

}
