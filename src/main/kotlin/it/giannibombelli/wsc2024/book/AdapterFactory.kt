package it.giannibombelli.wsc2024.book

import it.giannibombelli.wsc2024.Environment
import it.giannibombelli.wsc2024.book.adapter.InMemoryBookRepository
import it.giannibombelli.wsc2024.book.adapter.MongoBookRepository
import it.giannibombelli.wsc2024.book.domain.BookRepository

fun createBookRepository(): BookRepository =
    if (Environment().useInMemoryAdapters) InMemoryBookRepository() else MongoBookRepository()
