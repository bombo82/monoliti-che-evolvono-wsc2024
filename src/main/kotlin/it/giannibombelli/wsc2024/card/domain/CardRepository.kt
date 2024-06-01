package it.giannibombelli.wsc2024.card.domain

interface CardRepository {
    fun save(cardAggregate: CardAggregate)
    fun getById(cardId: CardId): CardAggregate?
}
