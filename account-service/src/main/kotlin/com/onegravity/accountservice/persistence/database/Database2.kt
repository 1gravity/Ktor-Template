package com.onegravity.accountservice.persistence.database

import com.onegravity.accountservice.persistence.exposed.model.Account


interface Database2 {

    fun account(): DatabaseOperations<Account>

}