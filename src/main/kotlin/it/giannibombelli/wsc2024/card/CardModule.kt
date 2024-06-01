package it.giannibombelli.wsc2024.card

import io.ktor.server.application.*
import it.giannibombelli.wsc2024.card.adapter.CardInProcessControllerForBookModule
import it.giannibombelli.wsc2024.card.adapter.cardHttpController
import it.giannibombelli.wsc2024.card.domain.BookDepositedUseCase
import it.giannibombelli.wsc2024.card.domain.CardRepository
import it.giannibombelli.wsc2024.card.domain.CreateCardUseCase
import it.giannibombelli.wsc2024.card.domain.QueryService
import it.giannibombelli.wsc2024.common.adapter.SoftworkUuidWrapper
import it.giannibombelli.wsc2024.common.domain.UuidWrapper

const val MODULE_NAME = "card"

val uuidWrapper: UuidWrapper = SoftworkUuidWrapper()
val repository: CardRepository = createCardRepository()
val cardInProcessControllerForBookModule = CardInProcessControllerForBookModule(BookDepositedUseCase(repository))

fun Application.cardModule() {
    cardHttpController(
        CreateCardUseCase(uuidWrapper, repository),
        QueryService(repository)
    )
}
