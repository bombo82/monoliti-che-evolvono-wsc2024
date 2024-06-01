plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "wsc2024"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("jvm", "21")
            version("kotlin", "1.9.22")
            version("ktorVersion", "2.3.11")
            version("serializationVersion", "1.6.3")
            version("logbackVersion", "1.5.6")
            version("uuidVersion", "0.0.25")
            version("mongodbVersion", "5.1.0")
            version("rabbitmqVersion", "5.21.0")
            version("mockkVersion", "1.13.11")

            plugin("jvm", "kotlin('jvm')").versionRef("kotlin")
            plugin("serialization", "plugin.serialization").versionRef("kotlin")

            library("ktor-server-core", "io.ktor", "ktor-server-core").versionRef("ktorVersion")
            library("ktor-server-cio", "io.ktor", "ktor-server-cio").versionRef("ktorVersion")
            library(
                "ktor-server-content-negotiation",
                "io.ktor",
                "ktor-server-content-negotiation"
            ).versionRef("ktorVersion")
            library(
                "ktor-serialization-kotlinx-json",
                "io.ktor",
                "ktor-serialization-kotlinx-json"
            ).versionRef("ktorVersion")
            library(
                "kotlinx-serialization-json",
                "org.jetbrains.kotlinx",
                "kotlinx-serialization-json"
            ).versionRef("serializationVersion")
            library("logback-classic", "ch.qos.logback", "logback-classic").versionRef("logbackVersion")
            library("uuid", "app.softwork", "kotlinx-uuid-core").versionRef("uuidVersion")
            library("mongodb-driver", "org.mongodb", "mongodb-driver-kotlin-coroutine").versionRef("mongodbVersion")
            library("mongodb-bson", "org.mongodb", "bson-kotlinx").versionRef("mongodbVersion")
            library("rabbitmq", "com.rabbitmq", "amqp-client").versionRef("rabbitmqVersion")
            library("ktor-client-core", "io.ktor", "ktor-client-core").versionRef("ktorVersion")
            library("ktor-client-cio", "io.ktor", "ktor-client-cio").versionRef("ktorVersion")
            library(
                "ktor-client-content-negotiation",
                "io.ktor",
                "ktor-client-content-negotiation"
            ).versionRef("ktorVersion")

            library("kotlin-test", "org.jetbrains.kotlin", "kotlin-test").versionRef("kotlin")
            library("mockk", "io.mockk", "mockk").versionRef("mockkVersion")
            library("ktor-server-test-host", "io.ktor", "ktor-server-test-host").versionRef("ktorVersion")

            bundle(
                "ktor-server-common",
                listOf(
                    "ktor-server-core",
                    "ktor-server-cio",
                    "ktor-server-content-negotiation",
                    "ktor-serialization-kotlinx-json",
                    "logback-classic"
                )
            )
            bundle("ktor-client-common", listOf("ktor-client-core", "ktor-client-cio"))
            bundle("mongodb", listOf("mongodb-driver", "mongodb-bson"))

            bundle(
                "ktor-test", listOf(
                    "kotlin-test",
                    "mockk",
                    "ktor-server-test-host",
                    "ktor-client-content-negotiation"
                )
            )
        }
    }
}
