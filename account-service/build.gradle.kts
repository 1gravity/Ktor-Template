import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.onegravity.Dependency

val applicationClass = "com.onegravity.accountservice.ApplicationKt"

plugins {
    application
    kotlin("jvm")

    // enable these two for kotlinx.serialization
    kotlin("plugin.serialization")
    kotlin("kapt")

    // for unknown reasons we need to use the fully qualified name for the plugin versions
    // importing com.onegravity.Plugin doesn't help
    com.onegravity.Plugin.modulePlugins.forEach { (n, v) -> id(n) version v }
}

dependencies {
    implementation(project(":common"))

    Dependency.implementation.forEach(::implementation)
    Dependency.runtime.forEach(::runtimeOnly)
    Dependency.testImplementation.forEach(::testImplementation)
}

application {
    mainClass.set(applicationClass)
}

tasks {
    // alternative syntax: "shadowJar"(ShadowJar::class) {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set(project.name)
        archiveClassifier.set("")
        archiveVersion.set("")
        mergeServiceFiles()
        project.setProperty("mainClassName", applicationClass)
        manifest {
            attributes(mapOf("Main-Class" to applicationClass))
        }
    }
}

tasks.register("stage") {
    dependsOn("assemble", "clean")
    mustRunAfter("clean")
}
