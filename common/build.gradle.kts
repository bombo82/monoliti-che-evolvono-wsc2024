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
    implementation(libs.uuid)
}

tasks.test {
    useJUnitPlatform()
}