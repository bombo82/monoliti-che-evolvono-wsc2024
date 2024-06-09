package it.giannibombelli.wsc2024.card

import it.giannibombelli.wsc2024.card.adapter.InMemoryCardRepository
import it.giannibombelli.wsc2024.card.adapter.MongoCardRepository
import it.giannibombelli.wsc2024.card.domain.CardRepository
import it.giannibombelli.wsc2024.common.Environment

fun createCardRepository(): CardRepository =
    if (Environment().useInMemoryAdapters) InMemoryCardRepository() else MongoCardRepository()
