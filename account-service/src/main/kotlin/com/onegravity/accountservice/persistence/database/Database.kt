package com.onegravity.accountservice.persistence.database

import com.onegravity.accountservice.persistence.model.account.AccountDao
import com.onegravity.accountservice.persistence.model.customer.CustomerDao

interface Database {

    fun accountDao(): AccountDao

    fun customerDao(): CustomerDao

}