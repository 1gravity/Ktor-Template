package com.onegravity.accountservice.persistence.model.ktorm

import com.onegravity.accountservice.persistence.model.DaoProvider
import com.onegravity.accountservice.util.DatabaseConfig
import com.onegravity.accountservice.persistence.database.dataSource
import com.onegravity.accountservice.persistence.database.migrate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import org.slf4j.Logger

class KtormDaoProvider(
    config: DatabaseConfig,
    logLevel: LogLevel = LogLevel.WARN,
    cleanDB: Boolean = false
) : DaoProvider, KoinComponent {

    private val logger by inject<Logger>()

    private val database: org.ktorm.database.Database by lazy {
        logger.debug("database url:      ${config.url}")
        logger.debug("database driver:   ${config.driver}")
        logger.debug("database user:     ${config.userName}")
        logger.debug("database password: ***")

        // with DataSource
        org.ktorm.database.Database.connect(
            dataSource(config.url, config.userName, config.password),
            logger = ConsoleLogger(logLevel)
        ).also {
            // run database migration
            migrate(cleanDB, config.url, config.userName, config.password)
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

    override fun accountDao() = AccountDao(database)

    override fun customerDao() = CustomerDao(accountDao(), database)

 }
