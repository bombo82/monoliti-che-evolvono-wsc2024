package it.giannibombelli.wsc2024.book.adapter

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import it.giannibombelli.wsc2024.book.domain.CardClient
import it.giannibombelli.wsc2024.common.Environment
import it.giannibombelli.wsc2024.common.domain.AggregateId
import kotlinx.coroutines.runBlocking

class HttpCardClient : CardClient {
    private val basePath = "${Environment().http.protocol}://${Environment().http.fqdn}:${Environment().http.port}"
    private var client: HttpClient = HttpClient(CIO)

    override fun bookDeposited(cardId: AggregateId): Unit = runBlocking {
        client.post("$basePath/card/$cardId/book-deposited")
    }
}
