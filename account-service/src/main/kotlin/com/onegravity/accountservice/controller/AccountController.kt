package com.onegravity.accountservice.controller

import com.onegravity.accountservice.route.response.Account as ResponseAccount
import com.onegravity.accountservice.route.request.Account as RequestAccount
import com.onegravity.accountservice.util.NotFoundException

interface AccountController {

    suspend fun getAccounts(): List<ResponseAccount>

    @Throws(NotFoundException::class)
    suspend fun getAccount(accountUUID: String): ResponseAccount

    @Throws(NotFoundException::class)
    suspend fun createAccount(account: RequestAccount): ResponseAccount

    @Throws(NotFoundException::class)
    suspend fun updateAccount(accountUUID: String, account: RequestAccount): ResponseAccount

    @Throws(NotFoundException::class)
    suspend fun deleteAccount(accountUUID: String): ResponseAccount

}