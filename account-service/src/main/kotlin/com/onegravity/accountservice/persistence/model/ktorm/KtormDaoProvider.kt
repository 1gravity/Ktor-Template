package com.onegravity.accountservice.persistence.model.ktorm

import com.onegravity.accountservice.persistence.model.DaoProvider
import com.onegravity.config.DatabaseConfig
import com.onegravity.persistence.getKtormDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ktorm.logging.LogLevel
import org.slf4j.Logger

class KtormDaoProvider(
    config: DatabaseConfig,
    logLevel: LogLevel = LogLevel.WARN,
    cleanDB: Boolean = false
) : DaoProvider, KoinComponent {

    private val logger by inject<Logger>()

    private val database by lazy { getKtormDatabase(config, logger, logLevel, cleanDB) }

    override fun accountDao() = AccountDao(database)

    override fun customerDao() = CustomerDao(accountDao(), database)

 }
