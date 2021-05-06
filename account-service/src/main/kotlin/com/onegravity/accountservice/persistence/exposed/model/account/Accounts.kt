package com.onegravity.accountservice.persistence.exposed.model.account

import com.onegravity.accountservice.persistence.model.account.AccountStatus
import org.jetbrains.exposed.dao.IntIdTable

object Accounts : IntIdTable("account") {
    val createdAt = datetime("created_at")
    val modifiedAt = datetime("modified_at")
    val accountUUID = varchar("account_uuid", 36)

    val status = enumeration("status", AccountStatus::class)
}
