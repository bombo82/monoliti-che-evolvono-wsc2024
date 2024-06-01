package it.giannibombelli.wsc2024.card.domain

import it.giannibombelli.wsc2024.common.domain.UuidWrapper

class CreateCardUseCase(private val uuidWrapper: UuidWrapper, private val repository: CardRepository) {

    fun generateUUID(): CardId = uuidWrapper.generateUUID()

    fun createCardCommand(cardId: CardId) {
        val card: CardAggregate = CardAggregate.create(cardId)
        repository.save(card)
    }

    fun queryCardById(cardId: CardId): CardAggregate {
        return repository.getById(cardId) ?: throw IllegalStateException()
    }
}
