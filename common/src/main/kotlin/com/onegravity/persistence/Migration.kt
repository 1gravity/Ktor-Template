package com.onegravity.persistence

import org.flywaydb.core.Flyway

fun migrate(cleanDB: Boolean, jdbcUrl: String, username: String, password: String) {
    val flyway = Flyway
        .configure()
        .dataSource(jdbcUrl, username, password)
        .load()

    if (cleanDB) flyway.clean()
    flyway.migrate()
}
