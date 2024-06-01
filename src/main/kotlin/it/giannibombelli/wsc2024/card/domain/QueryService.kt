package it.giannibombelli.wsc2024.card.domain

class QueryService(private val repository: CardRepository) {
    fun queryCardById(cardId: CardId): CardAggregate? = repository.getById(cardId)
}
