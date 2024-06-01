package it.giannibombelli.wsc2024.card.domain

import io.ktor.server.plugins.*

class BookDepositedUseCase(private val repository: CardRepository) {

    fun bookDeposited(cardId: CardId) {
        val cardAggregate: CardAggregate = repository.getById(cardId) ?: throw NotFoundException()

        cardAggregate.addPoints(1)

        repository.save(cardAggregate)
    }

}
