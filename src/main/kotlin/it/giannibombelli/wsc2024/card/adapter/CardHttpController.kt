package it.giannibombelli.wsc2024.card.adapter

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import it.giannibombelli.wsc2024.card.MODULE_NAME
import it.giannibombelli.wsc2024.card.domain.CardAggregate
import it.giannibombelli.wsc2024.card.domain.CardId
import it.giannibombelli.wsc2024.card.domain.CreateCardUseCase
import it.giannibombelli.wsc2024.card.domain.QueryService
import it.giannibombelli.wsc2024.common.Environment
import kotlinx.serialization.Serializable

fun Application.cardHttpController(createCardUseCase: CreateCardUseCase, queryService: QueryService) {
    val (protocol, fqdn, port) = Environment().http

    routing {
        route(MODULE_NAME) {
            post {
                val cardId: CardId = createCardUseCase.generateUUID()
                createCardUseCase.createCardCommand(cardId)

                call.response.status(HttpStatusCode.Created)
                call.response.headers.append(HttpHeaders.Location, "$protocol://$fqdn:$port/$MODULE_NAME/${cardId}")
                call.respond(createCardUseCase.queryCardById(cardId).mapToResponseDto())
            }
            get("/{cardId}") {
                val cardId = call.parameters["cardId"]
                requireNotNull(cardId)

                val card = queryService.queryCardById(cardId)

                if (card == null) {
                    call.response.status(HttpStatusCode.NotFound)
                } else {
                    call.response.status(HttpStatusCode.OK)
                    call.respond(card.mapToResponseDto())
                }
            }
        }
    }
}

@Serializable
data class CardResponseDto(val id: String, val balance: Int)

private fun CardAggregate.mapToResponseDto(): CardResponseDto = CardResponseDto(aggregateId, balance)
