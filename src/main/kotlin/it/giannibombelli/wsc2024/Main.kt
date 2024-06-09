package it.giannibombelli.wsc2024

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import it.giannibombelli.wsc2024.book.bookModule
import it.giannibombelli.wsc2024.card.cardModule
import it.giannibombelli.wsc2024.common.Environment
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

fun main() {
    (LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME) as Logger).level = Level.INFO

    embeddedServer(CIO, port = Environment().http.port, module = Application::mainModule).start(wait = true)
}

fun Application.mainModule() {
    install(IgnoreTrailingSlash)
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
        })
    }

    cardModule()
    bookModule()
}
