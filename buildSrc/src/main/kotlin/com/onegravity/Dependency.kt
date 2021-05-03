package com.onegravity

object Dependency {

    private const val ktorVersion = "1.5.3"
    private const val koinVersion = "3.0.1"
    private const val kotlinSerializationVersion = "1.1.0"

    private const val postgresVersion = "42.2.20"
    private const val ktormVersion = "3.3.0"
    private const val flywayVersion = "7.7.1"
    private const val hikariCPVersion = "4.0.3"

    private const val openApiGenVersion = "0.2-beta.17"
    private const val kotlinResultVersion = "1.1.11"
    private const val logbackVersion = "1.2.3"

    private const val kotestVersion = "4.4.3"
    private const val testcontainersVersion = "1.15.3"

    val implementation = listOf(
        // Ktor Server
        "io.ktor:ktor-server-core:$ktorVersion",
        "io.ktor:ktor-server-netty:$ktorVersion",

        // Monitoring metrics
        "io.ktor:ktor-metrics:$ktorVersion",
        "io.ktor:ktor-metrics-micrometer:$ktorVersion",

        // Koin / Dependency Injection
        "io.insert-koin:koin-ktor:$koinVersion",
        "io.insert-koin:koin-logger-slf4j:$koinVersion",

        // Kotlinx Serialization / Deserialization
        "io.ktor:ktor-serialization:$ktorVersion",
        "org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinSerializationVersion",
        // GSON Serialization / Deserialization
        "io.ktor:ktor-gson:$ktorVersion",

        // Ktorm (ORM)
        "org.ktorm:ktorm-core:$ktormVersion",
        "org.ktorm:ktorm-support-postgresql:$ktormVersion",
        "org.ktorm:ktorm-jackson:$ktormVersion",
        // HikariCP (Connection Pooling)
        "com.zaxxer:HikariCP:$hikariCPVersion",
        // Flyway (DB migration)
        "org.flywaydb:flyway-core:$flywayVersion",

        // OpenAPI Generator
        "com.github.1gravity:Ktor-OpenAPI-Generator:$openApiGenVersion",

        // Miscellaneous
        "com.michael-bull.kotlin-result:kotlin-result:$kotlinResultVersion",

        // Logging
        "ch.qos.logback:logback-classic:$logbackVersion"
    )

    val runtime = listOf(
        // Postgres
        "org.postgresql:postgresql:$postgresVersion"
    )

    val testImplementation = listOf(
        "io.ktor:ktor-server-tests:$ktorVersion",

        // Kotest
        "io.kotest:kotest-runner-junit5:$kotestVersion",
        "io.kotest:kotest-assertions-core:$kotestVersion",
        "io.kotest:kotest-property:$kotestVersion",
        "io.kotest:kotest-assertions-json:$kotestVersion",

        // Testcontainers for Postgres
        "org.testcontainers:junit-jupiter:$testcontainersVersion",
        "org.testcontainers:postgresql:$testcontainersVersion"
    )

}
