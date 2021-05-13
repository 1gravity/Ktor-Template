package com.onegravity.accountservice.persistence.model.exposed

import com.onegravity.accountservice.persistence.model.DaoProvider
import com.onegravity.config.DatabaseConfig
import com.onegravity.persistence.initExposedDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.Logger

class ExposedDaoProvider(
    config: DatabaseConfig,
    cleanDB: Boolean = false
) : DaoProvider, KoinComponent {

    private val logger by inject<Logger>()

    init {
        initExposedDatabase(config, logger, cleanDB)
    }

    override fun accountDao() = AccountDao()

    override fun customerDao() = CustomerDao(accountDao())

 }
