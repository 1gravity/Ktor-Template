package com.onegravity.accountservice.persistence.model.exposed

import com.onegravity.accountservice.persistence.model.AccountStatus
import org.jetbrains.exposed.dao.IntIdTable

object Accounts : IntIdTable("account") {
    val createdAt = datetime("created_at")
    val modifiedAt = datetime("modified_at")
    val accountUUID = varchar("account_uuid", 36)

    val status = postgresEnumeration<AccountStatus>("status", "account_status")
}
