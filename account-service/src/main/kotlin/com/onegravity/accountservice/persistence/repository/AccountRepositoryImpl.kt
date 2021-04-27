package com.onegravity.accountservice.persistence.repository

import com.onegravity.accountservice.persistence.database.Database
import com.onegravity.accountservice.persistence.model.account.AccountStatus
import com.onegravity.accountservice.util.NotFoundException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.toList
import java.time.Instant
import java.util.*
import com.onegravity.accountservice.route.request.Account as RequestAccount
import com.onegravity.accountservice.persistence.model.account.Account as PersistentAccount

class AccountRepositoryImpl : AccountRepository, KoinComponent {

    private val database by inject<Database>()

    override fun getAccounts() = database.accounts().toList()

    override fun getAccount(accountUUID: String?) =
        accountUUID
            ?.run { database.accounts().firstOrNull { it.accountUUID eq accountUUID } }
            ?: throw NotFoundException("Account with uuid $accountUUID not found")

    override fun createAccount(account: RequestAccount): PersistentAccount {
        val newAccount = PersistentAccount {
            val now = Instant.now()
            accountUUID = UUID.randomUUID().toString()
            createdAt = now
            modifiedAt = now
            status = account.status
        }
        database.accounts().add(newAccount)
        // if we return newAccount / PersistentAccount the time format will be different than what we store in the db
        return getAccount(newAccount.accountUUID)
    }

    override fun updateAccount(accountUUID: String?, account: RequestAccount) =
        getAccount(accountUUID)
            .also {
                it.status = account.status
                it.modifiedAt = Instant.now()
                it.flushChanges()
            }

    override fun deleteAccount(accountUUID: String?) =
        getAccount(accountUUID)
            .also {
                it.status = AccountStatus.Deleted
                it.modifiedAt = Instant.now()
                it.flushChanges()
            }

}
