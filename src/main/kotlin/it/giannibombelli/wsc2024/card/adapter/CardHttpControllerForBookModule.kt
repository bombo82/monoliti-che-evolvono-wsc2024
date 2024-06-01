package it.giannibombelli.wsc2024.card.adapter

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import it.giannibombelli.wsc2024.card.MODULE_NAME
import it.giannibombelli.wsc2024.card.domain.BookDepositedUseCase

fun Application.cardHttpControllerForBookModule(bookDepositedUseCase: BookDepositedUseCase) {
    routing {
        route(MODULE_NAME) {
            post("/{cardId}/book-deposited") {
                val cardId = call.parameters["cardId"]
                requireNotNull(cardId)

                bookDepositedUseCase.bookDeposited(cardId)

                call.response.status(HttpStatusCode.NoContent)
            }
        }
    }
}
