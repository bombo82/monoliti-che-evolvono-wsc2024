package it.giannibombelli.wsc2024.common.domain

typealias AggregateId = String

interface Aggregate {
    val aggregateId: AggregateId

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}
