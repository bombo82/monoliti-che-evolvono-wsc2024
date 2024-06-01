package it.giannibombelli.wsc2024.book.adapter

import it.giannibombelli.wsc2024.book.domain.CardClient
import it.giannibombelli.wsc2024.card.cardInProcessControllerForBookModule
import it.giannibombelli.wsc2024.common.domain.AggregateId

class InProcessCardClient : CardClient {

    override fun bookDeposited(cardId: AggregateId) {
        cardInProcessControllerForBookModule.bookDeposited(cardId)
    }
}
