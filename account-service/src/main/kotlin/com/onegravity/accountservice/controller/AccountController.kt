package com.onegravity.accountservice.controller

import com.onegravity.accountservice.route.model.account.CreateAccount
import com.onegravity.accountservice.route.model.account.ResponseAccount
import com.onegravity.accountservice.route.model.account.UpdateAccount
import com.onegravity.util.NotFoundException

interface AccountController {

    suspend fun getAccounts(): List<ResponseAccount>

    @Throws(NotFoundException::class)
    suspend fun getAccount(accountUUID: String): ResponseAccount

    @Throws(NotFoundException::class)
    suspend fun createAccount(account: CreateAccount): ResponseAccount

    @Throws(NotFoundException::class)
    suspend fun updateAccount(accountUUID: String, account: UpdateAccount): ResponseAccount

    @Throws(NotFoundException::class)
    suspend fun deleteAccount(accountUUID: String): ResponseAccount

}