package com.onegravity.accountservice.persistence.model.ktorm

import com.onegravity.accountservice.persistence.model.AccountStatus
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.timestamp
import org.ktorm.schema.varchar
import org.ktorm.support.postgresql.pgEnum

object Accounts : Table<Account>("account") {
    val id = int("id").primaryKey().bindTo { it.id }
    val createdAt = timestamp("created_at").bindTo { it.createdAt }
    val modifiedAt = timestamp("modified_at").bindTo { it.modifiedAt }
    val accountUUID = varchar("account_uuid").bindTo { it.accountUUID }

    val status = pgEnum<AccountStatus>("status").bindTo { it.status }
}
