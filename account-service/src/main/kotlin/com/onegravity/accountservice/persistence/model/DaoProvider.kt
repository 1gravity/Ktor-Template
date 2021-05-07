package com.onegravity.accountservice.persistence.model

import com.onegravity.accountservice.route.model.account.CreateAccount
import com.onegravity.accountservice.route.model.account.ResponseAccount
import com.onegravity.accountservice.route.model.account.UpdateAccount
import com.onegravity.accountservice.route.model.customer.CreateCustomer
import com.onegravity.accountservice.route.model.customer.ResponseCustomer
import com.onegravity.accountservice.route.model.customer.UpdateCustomer

interface DaoProvider {

    fun accountDao(): Dao<ResponseAccount, CreateAccount, UpdateAccount>

    fun customerDao(): Dao<ResponseCustomer, CreateCustomer, UpdateCustomer>

}
