package it.giannibombelli.wsc2024.book.domain

import it.giannibombelli.wsc2024.common.domain.AggregateId
import it.giannibombelli.wsc2024.common.domain.UuidWrapper

class DepositBookUseCase(
    private val uuidWrapper: UuidWrapper,
    private val repository: BookRepository,
    private val cardClient: CardClient
) {

    fun generateUUID(): BookId = uuidWrapper.generateUUID()

    fun depositBookCommand(bookId: BookId, name: String, author: String, cardId: AggregateId) {
        val book: BookAggregate = BookAggregate.create(bookId, name, author)
        repository.save(book)

        cardClient.bookDeposited(cardId)
    }

    fun getBookById(bookId: BookId): BookAggregate {
        return repository.getById(bookId) ?: throw IllegalStateException()
    }
}
