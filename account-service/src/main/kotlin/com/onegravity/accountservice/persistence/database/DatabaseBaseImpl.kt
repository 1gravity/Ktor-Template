package com.onegravity.accountservice.persistence.database

import com.onegravity.accountservice.persistence.exposed.model.Account
import com.onegravity.accountservice.persistence.exposed.model.Customer
import com.onegravity.accountservice.persistence.model.account.Accounts
import com.onegravity.accountservice.persistence.model.customer.Customers
import com.onegravity.accountservice.persistence.model.device.Devices
import com.onegravity.accountservice.persistence.model.email.Emails
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ktorm.entity.sequenceOf
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import org.slf4j.Logger

abstract class DatabaseBaseImpl(logLevel: LogLevel = LogLevel.WARN, cleanDB: Boolean = false) : Database, KoinComponent {

    private val logger by inject<Logger>()

    abstract val url: String
    abstract val driver: String
    abstract val user: String
    abstract val password: String

    private val dataSource by lazy {
        val config = HikariConfig().apply {
            jdbcUrl = this@DatabaseBaseImpl.url
            username = this@DatabaseBaseImpl.user
            password = this@DatabaseBaseImpl.password
            addDataSourceProperty("cachePrepStmts", "true")
            addDataSourceProperty("prepStmtCacheSize", "250")
            addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        }
        HikariDataSource(config)
    }

    private val database2 by lazy {
        logger.debug("exposed database url:      $url")
        logger.debug("exposed database driver:   $driver")
        logger.debug("exposed database user:     $user")
        logger.debug("exposed database password: ***")

        // with DataSource
        org.jetbrains.exposed.sql.Database.connect(
            dataSource
        ).also {
            transaction {
                SchemaUtils.create(Account, Customer)
                Account.selectAll().forEach {
                    logger.error("uuid: ${it[Account.accountUUID]}")
                }
                Customer.selectAll().forEach {
                    logger.error("uuid: ${it[Customer.customerUUID]}")
                    logger.error("firstName: ${it[Customer.firstName]}")
                    logger.error("lastName: ${it[Customer.lastName]}")
                    logger.error("accountId: ${it[Customer.accountId]}")
                }
                Customer.insert {
                }
            }
        }
    }

    private val database by lazy {
        database2
        logger.debug("database url:      $url")
        logger.debug("database driver:   $driver")
        logger.debug("database user:     $user")
        logger.debug("database password: ***")

        // with DataSource
        org.ktorm.database.Database.connect(
            dataSource,
            logger = ConsoleLogger(logLevel)
        ).also {
            // enable this if you want to erase the Flyway migration history (tests need to)
            if (cleanDB) flyway.clean()
            // run database migration
            flyway.migrate()
        }

        // without DataSource
//        org.ktorm.database.Database.connect(
//            url = url,
//            driver = driver,
//            user = user,
//            password = password,
//            logger = ConsoleLogger(LogLevel.DEBUG)
//        )
    }

    private val flyway: Flyway by lazy {
        Flyway
            .configure()
            .dataSource(url, user, password)
            .load()
    }

    override fun accounts() = database.sequenceOf(Accounts)

    override fun customers() = database.sequenceOf(Customers, withReferences = true)

    override fun emails() = database.sequenceOf(Emails, withReferences = true)

    override fun devices() = database.sequenceOf(Devices, withReferences = true)

}
