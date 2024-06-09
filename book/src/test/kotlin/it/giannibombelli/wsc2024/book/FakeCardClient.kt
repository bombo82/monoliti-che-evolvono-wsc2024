package it.giannibombelli.wsc2024.book

import it.giannibombelli.wsc2024.book.domain.CardClient
import it.giannibombelli.wsc2024.common.domain.AggregateId

class FakeCardClient : CardClient {

    override fun bookDeposited(cardId: AggregateId) = Unit
}
