package com.onegravity

object Dependency {

    private const val ktorVersion = "1.5.3"
    private const val koinVersion = "3.0.1"
    private const val kotlinSerializationVersion = "1.1.0"

    private const val postgresVersion = "42.2.20"
    private const val ktormVersion = "3.3.0"
    private const val exposedVersion = "0.17.13"
    private const val flywayVersion = "7.7.1"
    private const val hikariCPVersion = "4.0.3"

    private const val openApiGenVersion = "0.2-beta.17"
    private const val kotlinResultVersion = "1.1.11"
    private const val logbackVersion = "1.2.3"
    private const val dotenvVersion = "6.2.2"

    private const val kotestVersion = "4.4.3"
    private const val testcontainersVersion = "1.15.3"

    val implementation = mapOf(
        // Ktor Server
        "io.ktor:ktor-server-core" to ktorVersion,
        "io.ktor:ktor-server-netty" to ktorVersion,

        // Monitoring metrics
        "io.ktor:ktor-metrics" to ktorVersion,
        "io.ktor:ktor-metrics-micrometer" to ktorVersion,

        // Koin / Dependency Injection
        "io.insert-koin:koin-ktor" to koinVersion,
        "io.insert-koin:koin-logger-slf4j" to koinVersion,

        // Kotlinx Serialization / Deserialization
        "io.ktor:ktor-serialization" to ktorVersion,
        "org.jetbrains.kotlinx:kotlinx-serialization-core" to kotlinSerializationVersion,
        // GSON Serialization / Deserialization
        "io.ktor:ktor-gson" to ktorVersion,

        // Postgres
        "org.postgresql:postgresql" to postgresVersion,
        // Ktorm (ORM)
        "org.ktorm:ktorm-core" to ktormVersion,
        "org.ktorm:ktorm-support-postgresql" to ktormVersion,
        "org.ktorm:ktorm-jackson" to ktormVersion,
        // Exposed (ORM)
        "org.jetbrains.exposed:exposed" to exposedVersion,
        // HikariCP (Connection Pooling)
        "com.zaxxer:HikariCP" to hikariCPVersion,
        // Flyway (DB migration)
        "org.flywaydb:flyway-core" to flywayVersion,

        // OpenAPI Generator
        "com.github.1gravity:Ktor-OpenAPI-Generator" to openApiGenVersion,

        // Miscellaneous
        "com.michael-bull.kotlin-result:kotlin-result" to kotlinResultVersion,
        "io.github.cdimascio:dotenv-kotlin" to dotenvVersion,

        // Logging
        "ch.qos.logback:logback-classic" to logbackVersion
    ).toStringList()

    val runtime = emptyList<String>()

    val testImplementation = mapOf(
        "io.ktor:ktor-server-tests" to ktorVersion,

        // Kotest
        "io.kotest:kotest-runner-junit5" to kotestVersion,
        "io.kotest:kotest-assertions-core" to kotestVersion,
        "io.kotest:kotest-property" to kotestVersion,
        "io.kotest:kotest-assertions-json" to kotestVersion,

        // Testcontainers for Postgres
        "org.testcontainers:junit-jupiter" to testcontainersVersion,
        "org.testcontainers:postgresql" to testcontainersVersion
    ).toStringList()

    private fun Map<*, *>.toStringList(): List<String> {
        val result = ArrayList<String>()
        keys.forEach {
            result.add("$it:${this[it]}")
        }
        return result
    }

}
