package com.onegravity.accountservice.persistence.exposed.model.account

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID

class Account(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, Account>(Accounts)

    var accountUUID by Accounts.accountUUID

    var createdAt by Accounts.createdAt
    var modifiedAt by Accounts.modifiedAt

    var status by Accounts.status
}
