package it.giannibombelli.wsc2024.card

import io.ktor.server.plugins.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import it.giannibombelli.wsc2024.card.adapter.CardInProcessControllerForBookModule
import it.giannibombelli.wsc2024.card.domain.BookDepositedUseCase
import it.giannibombelli.wsc2024.card.domain.CardAggregate
import it.giannibombelli.wsc2024.card.domain.CardRepository
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private const val STUB_UUID = "f2dff8c3-217f-4c0a-984c-7deac07aeffe"

class CardComponentForBookModuleTest {

    @Test
    fun `bookDeposited should throw an exception when card does not exist`() {
        val repository: CardRepository = mockk<CardRepository>()
        every { repository.getById(STUB_UUID) } returns null
        val controller = CardInProcessControllerForBookModule(BookDepositedUseCase(repository))

        assertThrows<NotFoundException> {
            controller.bookDeposited(STUB_UUID)
        }
    }

    @Test
    fun `bookDeposited should add points to card when a book is deposited`() {
        val repository: CardRepository = spyk<CardRepository>()
        val cardAggregate = CardAggregate.create(STUB_UUID)
        every { repository.getById(STUB_UUID) } returns cardAggregate
        val controller = CardInProcessControllerForBookModule(BookDepositedUseCase(repository))

        controller.bookDeposited(STUB_UUID)

        assertNotNull(cardAggregate)
        assertEquals(1, cardAggregate.balance)
        verify(exactly = 1) { repository.save(any()) }
    }
}
