group = "it.giannibombelli"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    kotlin("plugin.serialization") version libs.versions.kotlin.get()
}

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))

    implementation(libs.bundles.ktor.server.common)
    implementation(libs.bundles.ktor.client.common)
    implementation(libs.bundles.mongodb)

    testImplementation(project(":common-test"))
    testImplementation(libs.bundles.ktor.test)
}

tasks.test {
    useJUnitPlatform()
}

