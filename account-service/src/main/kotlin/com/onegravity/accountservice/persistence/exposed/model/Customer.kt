package com.onegravity.accountservice.persistence.exposed.model

import com.onegravity.accountservice.persistence.model.customer.CustomerStatus
import com.onegravity.accountservice.persistence.model.customer.Language
import org.jetbrains.exposed.sql.Table

object Customer : Table("customer") {
    val id = integer("id").autoIncrement().primaryKey()
    val createdAt = datetime("created_at")
    val modifiedAt = datetime("modified_at")
    val customerUUID = varchar("customer_uuid", 36)

    val firstName = varchar("customer_uuid", 127)
    val lastName = varchar("customer_uuid", 127)
    val status = enumeration("status", CustomerStatus::class)
    val language = enumeration("language", Language::class)

    val accountId = integer("account_id").uniqueIndex().references(Account.id)
}

/*
    val id = int("id").primaryKey().bindTo { it.id }
    val createdAt = timestamp("created_at").bindTo { it.createdAt }
    val modifiedAt = timestamp("modified_at").bindTo { it.modifiedAt }
    val customerUUID = varchar("customer_uuid").bindTo { it.customerUUID }

    val firstName = varchar("first_name").bindTo { it.firstName }
    val lastName = varchar("last_name").bindTo { it.lastName }
    val status = pgEnum<CustomerStatus>("status").bindTo { it.status }
    val language = pgEnum<Language>("language").bindTo { it.language }

    val accountId = int("account_id").references(Accounts) { it.account }
 */