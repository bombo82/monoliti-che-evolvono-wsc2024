package it.giannibombelli.wsc2024.card

import io.ktor.server.plugins.*
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import it.giannibombelli.wsc2024.card.domain.BookDepositedUseCase
import it.giannibombelli.wsc2024.card.domain.CardAggregate
import it.giannibombelli.wsc2024.card.domain.CardRepository
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test
import kotlin.test.assertEquals

private const val STUB_UUID = "f2dff8c3-217f-4c0a-984c-7deac07aeffe"

@ExtendWith(MockKExtension::class)
@MockKExtension.CheckUnnecessaryStub
class BookDepositedUseCaseTest {

    @Test
    fun `bookDeposited should throw an exception when card does not exist`() {
        val repository: CardRepository = mockk<CardRepository>()
        every { repository.getById(STUB_UUID) } returns null
        val bookDepositedUseCase = BookDepositedUseCase(repository)

        assertThrows<NotFoundException> {
            bookDepositedUseCase.bookDeposited(STUB_UUID)
        }
    }

    @Test
    fun `bookDeposited should increment balance when card exist`() {
        val repository: CardRepository = mockk<CardRepository>(relaxed = true)
        val cardAggregate = CardAggregate.create(STUB_UUID)
        every { repository.getById(STUB_UUID) } returns cardAggregate
        val bookDepositedUseCase = BookDepositedUseCase(repository)

        bookDepositedUseCase.bookDeposited(STUB_UUID)

        assertEquals(cardAggregate.balance, 1)
    }

    @Test
    fun `bookDeposited should save updated card`() {
        val repository: CardRepository = spyk<CardRepository>()
        val cardAggregate = CardAggregate.create(STUB_UUID)
        every { repository.getById(STUB_UUID) } returns cardAggregate
        val bookDepositedUseCase = BookDepositedUseCase(repository)

        bookDepositedUseCase.bookDeposited(STUB_UUID)

        verify { repository.save(cardAggregate) }
    }
}
