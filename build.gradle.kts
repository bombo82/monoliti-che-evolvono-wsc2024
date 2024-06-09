group = "it.giannibombelli"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    kotlin("plugin.serialization") version libs.versions.kotlin.get()
    id("io.ktor.plugin") version libs.versions.ktorVersion.get()
    id("application")
}

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("it.giannibombelli.wsc2024.MainKt")
}

dependencies {
    implementation(project(":common"))

    implementation(libs.bundles.ktor.server.common)
    implementation(libs.bundles.ktor.client.common)
    implementation(libs.uuid)
    implementation(libs.bundles.mongodb)

    testImplementation(libs.bundles.ktor.test)
    testImplementation(project(":common-test"))
}

tasks.test {
    useJUnitPlatform()
}
