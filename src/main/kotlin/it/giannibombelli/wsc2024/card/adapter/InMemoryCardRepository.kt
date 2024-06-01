package it.giannibombelli.wsc2024.card.adapter

import it.giannibombelli.wsc2024.card.domain.CardAggregate
import it.giannibombelli.wsc2024.card.domain.CardId
import it.giannibombelli.wsc2024.card.domain.CardRepository

class InMemoryCardRepository : CardRepository {
    private val aggregateMap: MutableMap<CardId, CardAggregate> = mutableMapOf()

    override fun save(aggregate: CardAggregate) {
        aggregateMap[aggregate.aggregateId] = aggregate
    }

    override fun getById(aggregateId: CardId): CardAggregate? {
        return aggregateMap[aggregateId]?.let {
            CardAggregate(it.aggregateId).apply {
                initialize(it.balance)
            }
        }
    }
}
