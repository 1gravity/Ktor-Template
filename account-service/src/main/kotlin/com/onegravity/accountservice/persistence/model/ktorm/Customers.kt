package com.onegravity.accountservice.persistence.model.ktorm

import com.onegravity.accountservice.persistence.model.CustomerStatus
import com.onegravity.accountservice.persistence.model.Language
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.timestamp
import org.ktorm.schema.varchar
import org.ktorm.support.postgresql.pgEnum

object Customers : Table<Customer>("customer") {
    val id = int("id").primaryKey().bindTo { it.id }
    val createdAt = timestamp("created_at").bindTo { it.createdAt }
    val modifiedAt = timestamp("modified_at").bindTo { it.modifiedAt }
    val customerUUID = varchar("customer_uuid").bindTo { it.customerUUID }

    val firstName = varchar("first_name").bindTo { it.firstName }
    val lastName = varchar("last_name").bindTo { it.lastName }
    val status = pgEnum<CustomerStatus>("status").bindTo { it.status }
    val language = pgEnum<Language>("language").bindTo { it.language }

    val accountId = int("account_id").references(Accounts) { it.account }
}
