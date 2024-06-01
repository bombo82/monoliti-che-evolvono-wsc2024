package it.giannibombelli.wsc2024.book.adapter

import com.mongodb.client.model.Filters
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import it.giannibombelli.wsc2024.Environment
import it.giannibombelli.wsc2024.book.MODULE_NAME
import it.giannibombelli.wsc2024.book.domain.BookAggregate
import it.giannibombelli.wsc2024.book.domain.BookId
import it.giannibombelli.wsc2024.book.domain.BookRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime

class MongoBookRepository : BookRepository {
    private val collection: MongoCollection<BookDocument>
    private val idFieldName: String = BookDocument::bookId.name

    init {
        val database = MongoClient.create(Environment().mongodbConnection).getDatabase(MODULE_NAME)
        collection = database.getCollection<BookDocument>("aggregate")

        runBlocking {
            collection.createIndex(
                Indexes.ascending(idFieldName),
                IndexOptions().unique(true)
            )
        }
    }

    override fun save(bookAggregate: BookAggregate): Unit = runBlocking {
        val filter = Filters.eq(idFieldName, bookAggregate.aggregateId)
        val documentInDb: BookDocument? = collection.find<BookDocument>(filter).firstOrNull()

        if (documentInDb == null) {
            collection.insertOne(bookAggregate.mapToDocument())
        } else {
            collection.replaceOne(filter, bookAggregate.mapToDocument(documentInDb.id, documentInDb.createdAt))
        }
    }

    override fun getById(bookId: BookId): BookAggregate? = runBlocking {
        val filter = Filters.eq(idFieldName, bookId)
        val documentInDb: BookDocument? = collection.find<BookDocument>(filter).firstOrNull()

        documentInDb?.let {
            BookAggregate(documentInDb.bookId).apply {
                initialize(documentInDb.name, documentInDb.author)
            }
        }
    }

    data class BookDocument(
        @BsonId
        val id: ObjectId,
        val bookId: String,
        val name: String,
        val author: String,
        val createdAt: LocalDateTime,
        val lastUpdatedAt: LocalDateTime
    )

    private fun BookAggregate.mapToDocument(): BookDocument {
        val now = LocalDateTime.now()
        return BookDocument(ObjectId(), aggregateId, name, author, now, now)
    }

    private fun BookAggregate.mapToDocument(objectId: ObjectId, createdAt: LocalDateTime): BookDocument =
        BookDocument(objectId, aggregateId, name, author, createdAt, LocalDateTime.now())
}
