package it.giannibombelli.wsc2024.book

import io.ktor.server.application.*
import it.giannibombelli.wsc2024.book.adapter.MongoBookRepository
import it.giannibombelli.wsc2024.book.adapter.bookHttpController
import it.giannibombelli.wsc2024.book.domain.BookRepository
import it.giannibombelli.wsc2024.book.domain.DepositBookUseCase
import it.giannibombelli.wsc2024.common.adapter.SoftworkUuidWrapper
import it.giannibombelli.wsc2024.common.domain.UuidWrapper

const val MODULE_NAME = "book"

fun Application.bookModule() {
    val uuidWrapper: UuidWrapper = SoftworkUuidWrapper()
    val repository: BookRepository = MongoBookRepository()

    bookHttpController(DepositBookUseCase(uuidWrapper, repository))
}
