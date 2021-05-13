import com.onegravity.Dependency

plugins {
    java
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    Dependency.implementation.forEach(::implementation)
    Dependency.testImplementation.forEach(::testImplementation)
}
