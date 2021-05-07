package com.onegravity.accountservice.persistence.model.exposed

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID

class Customer(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, Customer>(Customers)
    var customerUUID by Customers.customerUUID

    var createdAt by Customers.createdAt
    var modifiedAt by Customers.modifiedAt

    var status by Customers.status

    var firstName by Customers.firstName
    var lastName by Customers.lastName
    var language by Customers.language

    var account by Account referencedOn Customers.accountId
}
