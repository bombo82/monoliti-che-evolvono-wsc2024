package it.giannibombelli.wsc2024.book

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import it.giannibombelli.wsc2024.book.domain.BookAggregate
import it.giannibombelli.wsc2024.book.domain.BookRepository
import it.giannibombelli.wsc2024.book.domain.DepositBookUseCase
import it.giannibombelli.wsc2024.common.domain.UuidWrapper
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test
import kotlin.test.assertEquals

private const val STUB_UUID = "f2dff8c3-217f-4c0a-984c-7deac07aeffe"

@ExtendWith(MockKExtension::class)
@MockKExtension.CheckUnnecessaryStub
class DepositBookUseCaseTest {

    @Test
    fun `generateUUID should return a new UUID`() {
        val stubUuidWrapper: UuidWrapper = mockk<UuidWrapper>()
        every { stubUuidWrapper.generateUUID() } returns STUB_UUID
        val depositBookUseCase = DepositBookUseCase(stubUuidWrapper, mockk<BookRepository>())

        val bookId = depositBookUseCase.generateUUID()

        assertEquals(STUB_UUID, bookId)
    }

    @Test
    fun `depositBookCommand should save a new book`() {
        val repository: BookRepository = spyk<BookRepository>()
        val depositBookUseCase = DepositBookUseCase(mockk<UuidWrapper>(), repository)

        depositBookUseCase.depositBookCommand(STUB_UUID, "Running Serverless", "Gojko Adzic")

        verify(exactly = 1) { repository.save(BookAggregate.create(STUB_UUID, "Running Serverless", "Gojko Adzic")) }
    }

    @Test
    fun `queryBookById should throw an IllegalStateException when not found just deposited book`() {
        val repository: BookRepository = mockk<BookRepository>()
        every { repository.getById(STUB_UUID) } returns null
        val depositBookUseCase = DepositBookUseCase(mockk<UuidWrapper>(), repository)

        assertThrows<IllegalStateException> {
            depositBookUseCase.queryBookById(STUB_UUID)
        }
    }

    @Test
    fun `queryBookById should return book with given id`() {
        val repository: BookRepository = mockk<BookRepository>()
        val expected = BookAggregate.create(STUB_UUID, "Running Serverless", "Gojko Adzic")
        every { repository.getById(STUB_UUID) } returns expected
        val depositBookUseCase = DepositBookUseCase(mockk<UuidWrapper>(), repository)

        val aggregate = depositBookUseCase.queryBookById(STUB_UUID)

        assertEquals(STUB_UUID, aggregate.aggregateId)
        assertEquals("Running Serverless", aggregate.name)
        assertEquals("Gojko Adzic", aggregate.author)
    }
}
