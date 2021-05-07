package com.onegravity.accountservice.persistence.model.ktorm

import com.onegravity.accountservice.persistence.model.CustomerStatus
import com.onegravity.accountservice.persistence.model.Language
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
