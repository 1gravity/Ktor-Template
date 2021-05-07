package com.onegravity.accountservice.persistence.model.exposed

import com.onegravity.accountservice.persistence.model.AccountStatus
import com.onegravity.accountservice.persistence.model.Dao
import com.onegravity.accountservice.route.model.account.CreateAccount
import com.onegravity.accountservice.route.model.account.ResponseAccount
import com.onegravity.accountservice.route.model.account.UpdateAccount
import com.onegravity.accountservice.util.NotFoundException
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.time.Instant
import org.joda.time.Instant as JodaInstant
import java.util.*

fun DateTime.toJavaInstant(): Instant = Instant.ofEpochMilli(toInstant().millis)

fun Instant.toDateTime(): DateTime = JodaInstant(toEpochMilli()).toDateTime()

class AccountDao : Dao<ResponseAccount, CreateAccount, UpdateAccount> {

    override fun getAll() = transaction {
        Account.all()
            .toList()
            .map { toObject(it) }
    }

    @Throws(NotFoundException::class)
    override fun get(uuid: String) = transaction { toObject(getAccount(uuid)) }

    override fun insert(`object`: CreateAccount): ResponseAccount {
        return transaction {
            val newAccountUUID = UUID.randomUUID().toString()
            Account.new {
                val now = Instant.now()
                accountUUID = newAccountUUID
                createdAt = now.toDateTime()
                modifiedAt = now.toDateTime()
                status = `object`.status
            }

            // if we return newAccount / PersistentAccount the time format will be different than what we store in the db
            get(newAccountUUID)
        }
    }

    override fun update(`object`: UpdateAccount): ResponseAccount {
        return transaction {
            getAccount(`object`.accountUUID).apply {
                `object`.status?.run { status = this }
                modifiedAt = Instant.now().toDateTime()
            }

            get(`object`.accountUUID)
        }
    }

    override fun delete(uuid: String): ResponseAccount {
        return transaction {
            getAccount(uuid).apply {
                status = AccountStatus.Deleted
                modifiedAt = Instant.now().toDateTime()
            }

            get(uuid)
        }
    }

    @Throws(NotFoundException::class)
    fun getAccount(uuid: String) = Account
        .find { Accounts.accountUUID eq uuid }
        .firstOrNull()
        ?: throw NotFoundException("Account with uuid $uuid not found")

    @Throws(NotFoundException::class)
    fun getAccount(id: Int) = Account
        .find { Accounts.id eq id }
        .firstOrNull()
        ?: throw NotFoundException("Account with id $id not found")

    fun toObject(entity: Account) = entity.run {
        ResponseAccount(accountUUID, createdAt.toJavaInstant(), modifiedAt.toJavaInstant(), status)
    }

}
