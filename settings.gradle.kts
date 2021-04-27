rootProject.name = "ktor-backend"

include(
    "account-service"
    // more services go here
)

// referencing a local project goes like this:
//project(":OpenAPI-Generator").projectDir = File("../Ktor-OpenAPI-Generator")

pluginManagement {
    val kotlinVersion: String by settings
    val shadowVersion: String by settings
    val taskinfoVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        id("com.github.johnrengelman.shadow") version shadowVersion
        id("org.barfuin.gradle.taskinfo") version taskinfoVersion
    }
}
