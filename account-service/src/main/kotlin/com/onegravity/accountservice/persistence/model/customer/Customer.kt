package com.onegravity.accountservice.persistence.model.customer

import com.onegravity.accountservice.persistence.model.account.Account
import org.ktorm.entity.Entity
import java.time.Instant

interface Customer : Entity<Customer> {
    companion object : Entity.Factory<Customer>()

    val id: Int
    var customerUUID: String

    var createdAt: Instant
    var modifiedAt: Instant

    var status: CustomerStatus

    var firstName: String
    var lastName: String
    var language: Language

    var account: Account
}
