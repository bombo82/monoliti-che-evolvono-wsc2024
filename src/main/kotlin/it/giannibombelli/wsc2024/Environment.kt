package it.giannibombelli.wsc2024

import java.lang.System.getenv

private const val DEFAULT_SERVER_PROTOCOL = "http"
private const val DEFAULT_SERVER_FQDN = "localhost"
private const val DEFAULT_SERVER_PORT = 8080
private const val DEFAULT_MONGODB_CONNECTION = "mongodb://root:example@localhost:27017"
private const val DEFAULT_RABBITMQ_HOST = "localhost"

data class Environment(
    val http: Http = Http(),
    val mongodbConnection: String = getenv("MONGODB_CONNECTION") ?: DEFAULT_MONGODB_CONNECTION,
    val rabbitMqHost: String = getenv("RABBITMQ_HOST") ?: DEFAULT_RABBITMQ_HOST
) {
    data class Http(
        val protocol: String = getenv("SERVER_PROTOCOL") ?: DEFAULT_SERVER_PROTOCOL,
        val fqdn: String = getenv("SERVER_FQDN") ?: DEFAULT_SERVER_FQDN,
        val port: Int = getenv("SERVER_PORT")?.toIntOrNull() ?: DEFAULT_SERVER_PORT,
    )
}
