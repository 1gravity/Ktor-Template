package com.onegravity.accountservice.persistence.exposed.model.customer

import com.onegravity.accountservice.persistence.exposed.model.account.Accounts
import com.onegravity.accountservice.persistence.model.customer.CustomerStatus
import com.onegravity.accountservice.persistence.model.customer.Language
import org.jetbrains.exposed.dao.IntIdTable

object Customers : IntIdTable("customer") {
    val createdAt = datetime("created_at")
    val modifiedAt = datetime("modified_at")
    val customerUUID = varchar("customer_uuid", 36)

    val firstName = varchar("customer_uuid", 127)
    val lastName = varchar("customer_uuid", 127)
    val status = enumeration("status", CustomerStatus::class)
    val language = enumeration("language", Language::class)

    val accountId = reference("account_id", Accounts.id).uniqueIndex()
}
