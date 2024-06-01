package it.giannibombelli.wsc2024.book.adapter

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import it.giannibombelli.wsc2024.Environment
import it.giannibombelli.wsc2024.book.MODULE_NAME
import it.giannibombelli.wsc2024.book.domain.BookAggregate
import it.giannibombelli.wsc2024.book.domain.DepositBookUseCase
import kotlinx.serialization.Serializable

fun Application.bookHttpController(depositBookUseCase: DepositBookUseCase) {
    val (protocol, fqdn, port) = Environment().http

    routing {
        route(MODULE_NAME) {
            post {
                val (cardId, name, author) = call.receive<DepositBookDto>()

                val bookId = depositBookUseCase.generateUUID()
                depositBookUseCase.depositBookCommand(bookId, name, author)

                call.response.status(HttpStatusCode.Created)
                call.response.headers.append(HttpHeaders.Location, "$protocol://$fqdn:$port/${MODULE_NAME}/${bookId}")
                call.respond(depositBookUseCase.queryBookById(bookId).mapToResponseDto())
            }
        }
    }
}

@Serializable
data class DepositBookDto(val cardId: String, val name: String, val author: String)

@Serializable
data class BookResponseDto(
    val id: String,
    val name: String,
    val author: String,
)

fun BookAggregate.mapToResponseDto(): BookResponseDto = BookResponseDto(aggregateId, name, author)
