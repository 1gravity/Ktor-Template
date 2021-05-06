package com.onegravity.accountservice.persistence.database

import com.onegravity.accountservice.persistence.model.account.AccountDao
import com.onegravity.accountservice.persistence.model.customer.CustomerDao
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
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
            isAutoCommit = true
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            addDataSourceProperty("cachePrepStmts", "true")
            addDataSourceProperty("prepStmtCacheSize", "250")
            addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        }
        HikariDataSource(config)
    }

    private val database: org.ktorm.database.Database by lazy {
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

    override fun accountDao() = AccountDao(database)

    override fun customerDao() = CustomerDao(accountDao(), database)

 }
