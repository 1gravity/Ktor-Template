package com.onegravity.accountservice.persistence.model.device

import com.onegravity.accountservice.persistence.model.customer.Customers
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.timestamp
import org.ktorm.schema.varchar
import org.ktorm.support.postgresql.pgEnum

object Devices : Table<Device>("device") {
    val id = int("id").primaryKey().bindTo { it.id }

    val createdAt = timestamp("created_at").bindTo { it.createdAt }
    val modifiedAt = timestamp("modified_at").bindTo { it.modifiedAt }
    val deviceId = varchar("device_id").bindTo { it.deviceId }

    val name = varchar("name").bindTo { it.name }
    val status = pgEnum<DeviceStatus>("status").bindTo { it.status }

    val customerId = int("customer_id").references(Customers) { it.customer }
}
