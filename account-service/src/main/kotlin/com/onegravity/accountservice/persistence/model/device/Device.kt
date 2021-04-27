package com.onegravity.accountservice.persistence.model.device

import com.onegravity.accountservice.persistence.model.customer.Customer
import org.ktorm.entity.Entity
import java.time.Instant

interface Device  : Entity<Device> {
    companion object : Entity.Factory<Device>()

    val id: Int

    var createdAt: Instant
    var modifiedAt: Instant

    var deviceId: String
    var name: String?
    var status: DeviceStatus

    var customer: Customer
}
