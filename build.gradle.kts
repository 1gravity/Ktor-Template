import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // for unknown reasons we need to use the fully qualified name for the plugin versions
    // importing com.onegravity.Plugin doesn't help
    com.onegravity.Plugin.topLevelPlugins.forEach { (n, v) -> id(n) version v }
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

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xuse-experimental=kotlin.ExperimentalStdlibApi")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
