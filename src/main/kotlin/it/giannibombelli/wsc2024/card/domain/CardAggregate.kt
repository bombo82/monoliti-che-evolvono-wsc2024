package it.giannibombelli.wsc2024.card.domain

import it.giannibombelli.wsc2024.common.domain.Aggregate
import it.giannibombelli.wsc2024.common.domain.AggregateId

typealias CardId = AggregateId

class CardAggregate(override val aggregateId: CardId) : Aggregate {

    companion object {
        fun create(cardId: CardId): CardAggregate = CardAggregate(cardId)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CardAggregate) return false

        if (aggregateId != other.aggregateId) return false

        return true
    }

    override fun hashCode(): Int {
        return aggregateId.hashCode()
    }
}
