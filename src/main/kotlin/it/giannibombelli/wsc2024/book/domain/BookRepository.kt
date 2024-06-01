package it.giannibombelli.wsc2024.book.domain

interface BookRepository {
    fun save(bookAggregate: BookAggregate)
    fun getById(bookId: BookId): BookAggregate?
}
