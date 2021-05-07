package com.onegravity.accountservice.persistence.model.ktorm

import com.onegravity.accountservice.persistence.model.AccountStatus
import org.ktorm.entity.Entity
import java.time.Instant

interface Account : Entity<Account> {
    companion object : Entity.Factory<Account>()

    val id: Int
    var accountUUID: String

    var createdAt: Instant
    var modifiedAt: Instant

    var status: AccountStatus
}

