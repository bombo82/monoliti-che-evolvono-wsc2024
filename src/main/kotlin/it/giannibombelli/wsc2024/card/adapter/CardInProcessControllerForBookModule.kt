package it.giannibombelli.wsc2024.card.adapter

import it.giannibombelli.wsc2024.card.domain.BookDepositedUseCase
import it.giannibombelli.wsc2024.card.domain.CardId

class CardInProcessControllerForBookModule(private val bookDepositedUseCase: BookDepositedUseCase) {

    fun bookDeposited(cardId: CardId) {
        bookDepositedUseCase.bookDeposited(cardId)
    }
}
