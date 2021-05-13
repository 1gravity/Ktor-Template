package com.onegravity.persistence

import com.onegravity.config.DatabaseConfig
import org.slf4j.Logger
import org.jetbrains.exposed.sql.Database

fun initExposedDatabase(
    config: DatabaseConfig,
    logger: Logger,
    cleanDB: Boolean = false
): Database {
    logger.debug("database url:      ${config.url}")
    logger.debug("database driver:   ${config.driver}")
    logger.debug("database user:     ${config.userName}")
    logger.debug("database password: ***")

    // with DataSource
    return Database.connect(
        dataSource(config.url, config.userName, config.password)
    ).apply {
        // run database migration
        migrate(cleanDB, config.url, config.userName, config.password)
    }
}
