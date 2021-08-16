package com.onegravity.accountservice.persistence.model.ktorm

import com.onegravity.accountservice.persistence.model.AccountStatus
import com.onegravity.accountservice.persistence.model.Dao
import com.onegravity.accountservice.route.model.account.CreateAccount
import com.onegravity.accountservice.route.model.account.ResponseAccount
import com.onegravity.accountservice.route.model.account.UpdateAccount
import com.onegravity.util.NotFoundException
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import java.time.Instant
import java.util.*

class AccountDao(private val database: Database) : Dao<ResponseAccount, CreateAccount, UpdateAccount> {

    override fun getAll() = database
        .sequenceOf(Accounts)
        .toList()
        .map { toObject(it) }

    @Throws(NotFoundException::class)
    override fun get(uuid: String) = toObject(getAccount(uuid))

    override fun insert(`object`: CreateAccount): ResponseAccount {
        val newAccount = Account {
            val now = Instant.now()
            accountUUID = UUID.randomUUID().toString()
            createdAt = now
            modifiedAt = now
            status = `object`.status
        }
        database.sequenceOf(Accounts).add(newAccount)
        // if we return newAccount / PersistentAccount the time format will be different than what we store in the db
        return get(newAccount.accountUUID)
    }

    override fun update(uuid: String, `object`: UpdateAccount): ResponseAccount {
        database.sequenceOf(Accounts)
            .firstOrNull { Accounts.accountUUID eq uuid }
            ?.apply {
                `object`.status?.run { status = this }
                modifiedAt = Instant.now()
                flushChanges()
            }
        return get(uuid)
    }

    override fun delete(uuid: String): ResponseAccount {
        database.sequenceOf(Accounts)
            .firstOrNull { Accounts.accountUUID eq uuid }
            ?.apply {
                status = AccountStatus.Deleted
                modifiedAt = Instant.now()
                flushChanges()
            }
        return get(uuid)
    }

    @Throws(NotFoundException::class)
    fun getAccount(uuid: String) = database.sequenceOf(Accounts)
        .firstOrNull { Accounts.accountUUID eq uuid }
        ?: throw NotFoundException("Account with uuid $uuid not found")

    fun toObject(entity: Account) = entity.run {
        ResponseAccount(accountUUID, createdAt, modifiedAt, status)
    }

}
