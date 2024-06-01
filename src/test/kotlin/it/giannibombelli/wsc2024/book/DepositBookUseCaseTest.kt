package it.giannibombelli.wsc2024.book

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import it.giannibombelli.wsc2024.book.domain.BookAggregate
import it.giannibombelli.wsc2024.book.domain.BookRepository
import it.giannibombelli.wsc2024.book.domain.CardClient
import it.giannibombelli.wsc2024.book.domain.DepositBookUseCase
import it.giannibombelli.wsc2024.common.domain.UuidWrapper
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test
import kotlin.test.assertEquals

private const val STUB_BOOK_UUID = "f2dff8c3-217f-4c0a-984c-7deac07aeffe"
private const val STUB_CARD_UUID = "2fdff8c3-217f-4c0a-984c-7deac07afeef"

@ExtendWith(MockKExtension::class)
@MockKExtension.CheckUnnecessaryStub
class DepositBookUseCaseTest {

    @Test
    fun `generateUUID should return a new UUID`() {
        val stubUuidWrapper: UuidWrapper = mockk<UuidWrapper>()
        every { stubUuidWrapper.generateUUID() } returns STUB_BOOK_UUID
        val depositBookUseCase = DepositBookUseCase(stubUuidWrapper, mockk<BookRepository>(), mockk<CardClient>())

        val bookId = depositBookUseCase.generateUUID()

        assertEquals(STUB_BOOK_UUID, bookId)
    }

    @Test
    fun `depositBookCommand should save a new book`() {
        val repository: BookRepository = spyk<BookRepository>()
        val depositBookUseCase =
            DepositBookUseCase(mockk<UuidWrapper>(), repository, mockk<CardClient>(relaxed = true))

        depositBookUseCase.depositBookCommand(STUB_BOOK_UUID, "Running Serverless", "Gojko Adzic", STUB_CARD_UUID)

        verify(exactly = 1) {
            repository.save(BookAggregate.create(STUB_BOOK_UUID, "Running Serverless", "Gojko Adzic"))
        }
    }

    @Test
    fun `depositBookCommand should notify the card module that a book has been deposited`() {
        val cardClient: CardClient = spyk<CardClient>()
        val depositBookUseCase =
            DepositBookUseCase(mockk<UuidWrapper>(), mockk<BookRepository>(relaxed = true), cardClient)

        depositBookUseCase.depositBookCommand(STUB_BOOK_UUID, "Running Serverless", "Gojko Adzic", STUB_CARD_UUID)

        verify(exactly = 1) { cardClient.bookDeposited(STUB_CARD_UUID) }
    }

    @Test
    fun `getBookById should throw an IllegalStateException when not found just deposited book`() {
        val repository: BookRepository = mockk<BookRepository>()
        every { repository.getById(STUB_BOOK_UUID) } returns null
        val depositBookUseCase = DepositBookUseCase(mockk<UuidWrapper>(), repository, mockk<CardClient>())

        assertThrows<IllegalStateException> {
            depositBookUseCase.getBookById(STUB_BOOK_UUID)
        }
    }

    @Test
    fun `getBookById should return book with given id`() {
        val repository: BookRepository = mockk<BookRepository>()
        val expected = BookAggregate.create(STUB_BOOK_UUID, "Running Serverless", "Gojko Adzic")
        every { repository.getById(STUB_BOOK_UUID) } returns expected
        val depositBookUseCase = DepositBookUseCase(mockk<UuidWrapper>(), repository, mockk<CardClient>())

        val aggregate = depositBookUseCase.getBookById(STUB_BOOK_UUID)

        assertEquals(STUB_BOOK_UUID, aggregate.aggregateId)
        assertEquals("Running Serverless", aggregate.name)
        assertEquals("Gojko Adzic", aggregate.author)
    }
}
