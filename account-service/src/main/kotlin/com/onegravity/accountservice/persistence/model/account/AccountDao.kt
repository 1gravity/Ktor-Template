package com.onegravity.accountservice.persistence.model.account

import com.onegravity.accountservice.persistence.model.Dao
import com.onegravity.accountservice.route.model.account.CreateAccount
import com.onegravity.accountservice.route.model.account.ResponseAccount
import com.onegravity.accountservice.route.model.account.UpdateAccount
import com.onegravity.accountservice.util.NotFoundException
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import java.time.Instant
import java.util.*

class AccountDao(private val database: Database) : Dao<Account, ResponseAccount, CreateAccount, UpdateAccount> {

    override fun getAll() = database
        .sequenceOf(Accounts)
        .toList()
        .map { toObject(it) }

    @Throws(NotFoundException::class)
    fun getAccount(uuid: String) = database.sequenceOf(Accounts)
        .firstOrNull { it.accountUUID eq uuid }
        ?: throw NotFoundException("Account with uuid $uuid not found")

    @Throws(NotFoundException::class)
    override fun get(uuid: String) = toObject(getAccount(uuid))

    override fun insert(account: CreateAccount): ResponseAccount {
        val newAccount = Account {
            val now = Instant.now()
            accountUUID = UUID.randomUUID().toString()
            createdAt = now
            modifiedAt = now
            status = account.status
        }
        database.sequenceOf(Accounts).add(newAccount)
        // if we return newAccount / PersistentAccount the time format will be different than what we store in the db
        return get(newAccount.accountUUID)
    }

    override fun update(account: UpdateAccount): ResponseAccount {
        database.sequenceOf(Accounts)
            .firstOrNull { it.accountUUID eq account.accountUUID }
            ?.apply {
                account.status?.run { status = this }
                modifiedAt = Instant.now()
                flushChanges()
            }
        return get(account.accountUUID)
    }

    override fun delete(uuid: String): ResponseAccount {
        database.sequenceOf(Accounts)
            .firstOrNull { it.accountUUID eq uuid }
            ?.apply {
                status = AccountStatus.Deleted
                modifiedAt = Instant.now()
                flushChanges()
            }
        return get(uuid)
    }

    override fun toObject(entity: Account) = entity.run {
        ResponseAccount(accountUUID, createdAt, modifiedAt, status)
    }

}
