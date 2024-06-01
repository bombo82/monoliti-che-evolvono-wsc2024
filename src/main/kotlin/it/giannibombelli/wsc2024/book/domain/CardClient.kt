package it.giannibombelli.wsc2024.book.domain

import it.giannibombelli.wsc2024.common.domain.AggregateId

interface CardClient {

    fun bookDeposited(cardId: AggregateId)
}
