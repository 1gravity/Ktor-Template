package com.onegravity.accountservice.persistence.model.exposed

import com.onegravity.accountservice.persistence.model.DaoProvider
import com.onegravity.accountservice.util.DatabaseConfig
import com.onegravity.accountservice.persistence.database.dataSource
import com.onegravity.accountservice.persistence.database.migrate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.Logger

class ExposedDaoProvider(
    config: DatabaseConfig,
    cleanDB: Boolean = false
) : DaoProvider, KoinComponent {

    private val logger by inject<Logger>()

    init {
        logger.debug("database url:      ${config.url}")
        logger.debug("database driver:   ${config.driver}")
        logger.debug("database user:     ${config.userName}")
        logger.debug("database password: ***")

        // with DataSource
        org.jetbrains.exposed.sql.Database.connect(
            dataSource(config.url, config.userName, config.password)
        ).also {
            // run database migration
            migrate(cleanDB, config.url, config.userName, config.password)
        }
    }

    override fun accountDao() = AccountDao()

    override fun customerDao() = CustomerDao(accountDao())

 }
