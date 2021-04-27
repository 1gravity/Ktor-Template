import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String by project
val ktorVersion: String by project
val koinVersion: String by project
val postgresVersion: String by project
val ktormVersion: String by project
val flywayVersion: String by project
val kotlinSerializationVersion: String by project
val logbackVersion: String by project

val kotestVersion: String by project
val testcontainersVersion: String by project

plugins {
    application
    kotlin("jvm")

    id("com.github.johnrengelman.shadow")

    // enable these two for kotlinx.serialization
    kotlin("plugin.serialization")
    kotlin("kapt")
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib", kotlinVersion))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinSerializationVersion")

    // Ktor
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")

    // Monitoring metrics
    implementation("io.ktor:ktor-metrics:$ktorVersion")
    implementation("io.ktor:ktor-metrics-micrometer:$ktorVersion")

    // Koin / Dependency Injection
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // Kotlinx Serialization / Deserialization
    implementation("io.ktor:ktor-serialization:$ktorVersion")

    // GSON Serialization / Deserialization
    implementation("io.ktor:ktor-gson:$ktorVersion")

    // Postgres
    runtimeOnly("org.postgresql:postgresql:$postgresVersion")

    // Ktorm (ORM)
    implementation("org.ktorm:ktorm-core:$ktormVersion")
    implementation("org.ktorm:ktorm-support-postgresql:$ktormVersion")
    implementation("org.ktorm:ktorm-jackson:$ktormVersion")

    // Flyway (DB migration)
    implementation("org.flywaydb:flyway-core:$flywayVersion")

    // OpenAPI Generator
    implementation("com.github.1gravity:Ktor-OpenAPI-Generator:-SNAPSHOT")
    // enable to use the local openapi.generator dependency
    // implementation(project(":OpenAPI-Generator", configuration = "default"))

    // Miscellaneous
    implementation("com.michael-bull.kotlin-result:kotlin-result:1.1.11")

    // Testing
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    // Kotest
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-json:$kotestVersion")
    // Testcontainers for Postgres
    implementation(platform("org.testcontainers:testcontainers-bom:$testcontainersVersion"))
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}

application {
    mainClass.set("com.onegravity.accountservice.application.ApplicationKt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xuse-experimental=kotlin.ExperimentalStdlibApi")
    }
}

tasks {
    // alternative syntax: "shadowJar"(ShadowJar::class) {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set(project.name)
        archiveClassifier.set("")
        archiveVersion.set("")
        mergeServiceFiles()
        project.setProperty("mainClassName", "com.onegravity.accountservice.application.ApplicationKt")
        manifest {
            attributes(mapOf("Main-Class" to "com.onegravity.accountservice.application.ApplicationKt"))
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
