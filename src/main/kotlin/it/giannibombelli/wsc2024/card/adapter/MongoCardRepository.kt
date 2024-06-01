package it.giannibombelli.wsc2024.card.adapter

import com.mongodb.client.model.Filters
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import it.giannibombelli.wsc2024.Environment
import it.giannibombelli.wsc2024.card.MODULE_NAME
import it.giannibombelli.wsc2024.card.domain.CardAggregate
import it.giannibombelli.wsc2024.card.domain.CardId
import it.giannibombelli.wsc2024.card.domain.CardRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime

class MongoCardRepository : CardRepository {
    private var collection: MongoCollection<CardDocument>
    private val idFieldName: String = CardDocument::cardId.name

    init {
        val database = MongoClient.create(Environment().mongodbConnection).getDatabase(MODULE_NAME)
        collection = database.getCollection<CardDocument>("aggregate")

        runBlocking {
            collection.createIndex(
                Indexes.ascending(idFieldName),
                IndexOptions().unique(true)
            )
        }
    }

    override fun save(cardAggregate: CardAggregate): Unit = runBlocking {
        val filter = Filters.eq(idFieldName, cardAggregate.aggregateId)
        val documentInDb: CardDocument? = collection.find<CardDocument>(filter).firstOrNull()

        if (documentInDb == null) {
            collection.insertOne(cardAggregate.mapToDocument())
        } else {
            collection.replaceOne(filter, cardAggregate.mapToDocument(documentInDb.id, documentInDb.createdAt))
        }
    }

    override fun getById(cardId: CardId): CardAggregate? = runBlocking {
        val filter = Filters.eq(idFieldName, cardId)
        val documentInDb: CardDocument? = collection.find<CardDocument>(filter).firstOrNull()

        documentInDb?.let {
            CardAggregate(documentInDb.cardId).apply {
                initialize(documentInDb.balance)
            }
        }
    }

    data class CardDocument(
        @BsonId
        val id: ObjectId,
        val cardId: String,
        val balance: Int,
        val createdAt: LocalDateTime,
        val lastUpdatedAt: LocalDateTime
    )

    private fun CardAggregate.mapToDocument(): CardDocument {
        val now = LocalDateTime.now()
        return CardDocument(ObjectId(), aggregateId, balance, now, now)
    }

    private fun CardAggregate.mapToDocument(objectId: ObjectId, createdAt: LocalDateTime): CardDocument =
        CardDocument(objectId, aggregateId, balance, createdAt, LocalDateTime.now())
}
