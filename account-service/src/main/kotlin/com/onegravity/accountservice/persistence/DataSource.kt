package com.onegravity.accountservice.persistence

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

fun dataSource(jdbcUrl: String, username: String, password: String): DataSource {
    val config = HikariConfig().apply {
        this.jdbcUrl = jdbcUrl
        this.username = username
        this.password = password
        isAutoCommit = true
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        addDataSourceProperty("cachePrepStmts", "true")
        addDataSourceProperty("prepStmtCacheSize", "250")
        addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
    }

    return HikariDataSource(config)
}