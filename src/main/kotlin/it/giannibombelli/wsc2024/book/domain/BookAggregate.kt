package it.giannibombelli.wsc2024.book.domain

import it.giannibombelli.wsc2024.common.domain.Aggregate
import it.giannibombelli.wsc2024.common.domain.AggregateId

typealias BookId = AggregateId

class BookAggregate(override val aggregateId: BookId) : Aggregate {
    private lateinit var _name: String
    val name: String
        get() = _name

    private lateinit var _author: String
    val author: String
        get() = _author

    fun BookAggregate.initialize(name: String, author: String) {
        this._name = name
        this._author = author
    }

    companion object {
        fun create(bookId: BookId, name: String, author: String): BookAggregate = BookAggregate(bookId).apply {
            initialize(name, author)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BookAggregate) return false

        if (aggregateId != other.aggregateId) return false

        return true
    }

    override fun hashCode(): Int {
        return aggregateId.hashCode()
    }
}
