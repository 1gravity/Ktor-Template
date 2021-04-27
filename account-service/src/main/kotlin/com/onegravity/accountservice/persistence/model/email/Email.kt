package com.onegravity.accountservice.persistence.model.email

import com.onegravity.accountservice.persistence.model.customer.Customer
import org.ktorm.entity.Entity
import java.time.Instant

interface Email : Entity<Email> {
    companion object : Entity.Factory<Email>()

    val id: Int

    var createdAt: Instant
    var modifiedAt: Instant

    var email: String
    var status: EmailStatus

    var customer: Customer

}
