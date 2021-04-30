package com.onegravity.accountservice.persistence.repository

import com.onegravity.accountservice.util.NotFoundException
import com.onegravity.accountservice.route.request.RequestAccount as RequestAccount
import com.onegravity.accountservice.persistence.model.account.Account as PersistentAccount

interface AccountRepository {

    fun getAccounts(): List<PersistentAccount>

    @Throws(NotFoundException::class)
    fun getAccount(accountUUID: String?): PersistentAccount

    @Throws(NotFoundException::class)
    fun createAccount(account: RequestAccount): PersistentAccount

    @Throws(NotFoundException::class)
    fun updateAccount(accountUUID: String?, account: RequestAccount): PersistentAccount

    @Throws(NotFoundException::class)
    fun deleteAccount(accountUUID: String?): PersistentAccount

}
