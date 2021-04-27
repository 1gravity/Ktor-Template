plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    // retrieve task dependencies and types, e.g. with ./gradlew tiTree assemble
    id("org.barfuin.gradle.taskinfo")
}

buildscript {
    repositories {
        mavenCentral()
        maven("https://dl.bintray.com/kotlin/ktor")
    }
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://dl.bintray.com/kotlin/ktor")
        maven("https://jitpack.io")
    }
}
gradle.taskGraph.whenReady(closureOf<TaskExecutionGraph> {
    println("Found task graph: $this")
    println("Found " + allTasks.size + " tasks.")
    allTasks.forEach { task ->
        println(task)
        task.dependsOn.forEach { dep ->
            println("  - $dep")
        }
    }
})