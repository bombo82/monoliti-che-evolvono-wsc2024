package it.giannibombelli.wsc2024.book.adapter

import it.giannibombelli.wsc2024.book.domain.BookAggregate
import it.giannibombelli.wsc2024.book.domain.BookId
import it.giannibombelli.wsc2024.book.domain.BookRepository

class InMemoryBookRepository : BookRepository {
    private val aggregateMap: MutableMap<BookId, BookAggregate> = mutableMapOf()

    override fun save(aggregate: BookAggregate) {
        aggregateMap[aggregate.aggregateId] = aggregate
    }

    override fun getById(aggregateId: BookId): BookAggregate? {
        return aggregateMap[aggregateId]?.let {
            BookAggregate(it.aggregateId).apply {
                initialize(it.name, it.author)
            }
        }
    }
}
