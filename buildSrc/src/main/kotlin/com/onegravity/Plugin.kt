package com.onegravity

object Plugin {

    const val kotlinVersion = "1.5.31"
    private const val shadowVersion = "7.1.0"

    // https://plugins.gradle.org/plugin/org.barfuin.gradle.taskinfo
    // retrieve task dependencies and types, e.g. with ./gradlew tiTree assemble
    private const val taskinfoVersion = "1.0.5"

    val topLevelPlugins = mapOf(
        "org.jetbrains.kotlin.jvm" to kotlinVersion,
        "org.jetbrains.kotlin.plugin.serialization" to kotlinVersion,
        "org.barfuin.gradle.taskinfo" to taskinfoVersion
    )

    val modulePlugins = mapOf(
        "com.github.johnrengelman.shadow" to shadowVersion
    )

}
