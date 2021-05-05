package com.onegravity.accountservice.persistence.exposed.model

import com.onegravity.accountservice.persistence.model.account.AccountStatus
import org.jetbrains.exposed.sql.Table

object Account : Table("account") {
    val id = integer("id").autoIncrement().primaryKey()
    val createdAt = datetime("created_at")
    val modifiedAt = datetime("modified_at")
    val accountUUID = varchar("account_uuid", 36)
    val status = enumeration("status", AccountStatus::class)
}
