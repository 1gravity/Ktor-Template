package com.onegravity.accountservice.persistence.database

import com.onegravity.accountservice.persistence.model.account.Account
import com.onegravity.accountservice.persistence.model.account.Accounts
import com.onegravity.accountservice.persistence.model.customer.Customer
import com.onegravity.accountservice.persistence.model.customer.Customers
import com.onegravity.accountservice.persistence.model.device.Device
import com.onegravity.accountservice.persistence.model.device.Devices
import com.onegravity.accountservice.persistence.model.email.Email
import com.onegravity.accountservice.persistence.model.email.Emails
import org.ktorm.entity.EntitySequence

interface Database {

    fun account(): DatabaseOperations<Account>

    fun accounts(): EntitySequence<Account, Accounts>

    fun customers(): EntitySequence<Customer, Customers>

    fun emails(): EntitySequence<Email, Emails>

    fun devices(): EntitySequence<Device, Devices>

}