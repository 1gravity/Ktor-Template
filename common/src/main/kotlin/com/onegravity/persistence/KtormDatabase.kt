package com.onegravity.persistence

import com.onegravity.config.DatabaseConfig
import org.ktorm.logging.ConsoleLogger
import org.ktorm.logging.LogLevel
import org.slf4j.Logger
import org.ktorm.database.Database

fun getKtormDatabase(
    config: DatabaseConfig,
    logger: Logger,
    logLevel: LogLevel = LogLevel.WARN,
    cleanDB: Boolean = false
): Database {

    logger.debug("database url:      ${config.url}")
    logger.debug("database driver:   ${config.driver}")
    logger.debug("database user:     ${config.userName}")
    logger.debug("database password: ***")

    // with DataSource
    return Database.connect(
        dataSource(config.url, config.userName, config.password),
        logger = ConsoleLogger(logLevel)
    ).apply {
        // run database migration
        migrate(cleanDB, config.url, config.userName, config.password)
    }

    // without DataSource
//        return Database.connect(
//            url = url,
//            driver = driver,
//            user = user,
//            password = password,
//            logger = ConsoleLogger(LogLevel.DEBUG)
//        )

}