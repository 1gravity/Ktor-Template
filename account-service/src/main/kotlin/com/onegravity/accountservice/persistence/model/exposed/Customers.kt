package com.onegravity.accountservice.persistence.model.exposed

import com.onegravity.accountservice.persistence.model.CustomerStatus
import com.onegravity.accountservice.persistence.model.Language
import org.jetbrains.exposed.dao.IntIdTable

object Customers : IntIdTable("customer") {
    val createdAt = datetime("created_at")
    val modifiedAt = datetime("modified_at")
    val customerUUID = varchar("customer_uuid", 36)

    val firstName = varchar("first_name", 127)
    val lastName = varchar("last_name", 127)
    val status = postgresEnumeration<CustomerStatus>("status", "customer_status")
    val language = postgresEnumeration<Language>("language", "language_enum")

    val accountId = reference("account_id", Accounts)
}
