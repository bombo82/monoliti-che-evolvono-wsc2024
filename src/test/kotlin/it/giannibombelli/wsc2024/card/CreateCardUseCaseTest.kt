package it.giannibombelli.wsc2024.card

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import it.giannibombelli.wsc2024.card.domain.CardAggregate
import it.giannibombelli.wsc2024.card.domain.CardRepository
import it.giannibombelli.wsc2024.card.domain.CreateCardUseCase
import it.giannibombelli.wsc2024.common.domain.UuidWrapper
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test
import kotlin.test.assertEquals

private const val STUB_UUID = "f2dff8c3-217f-4c0a-984c-7deac07aeffe"

@ExtendWith(MockKExtension::class)
@MockKExtension.CheckUnnecessaryStub
class CreateCardUseCaseTest {

    @Test
    fun `generateUUID should return a new UUID`() {
        val stubUuidWrapper: UuidWrapper = mockk<UuidWrapper>()
        every { stubUuidWrapper.generateUUID() } returns STUB_UUID
        val createCardUseCase = CreateCardUseCase(stubUuidWrapper, mockk<CardRepository>())

        val cardId = createCardUseCase.generateUUID()

        assertEquals(STUB_UUID, cardId)
        verify(exactly = 1) { stubUuidWrapper.generateUUID() }
    }

    @Test
    fun `createCardCommand should save a new Card`() {
        val repository: CardRepository = spyk<CardRepository>()
        val createCardUseCase = CreateCardUseCase(mockk<UuidWrapper>(), repository)

        createCardUseCase.createCardCommand(STUB_UUID)

        verify(exactly = 1) { repository.save(any()) }
    }

    @Test
    fun `queryCardById should throw an IllegalStateException when not found just created card`() {
        val repository: CardRepository = mockk<CardRepository>()
        every { repository.getById(STUB_UUID) } returns null
        val createCardUseCase = CreateCardUseCase(mockk<UuidWrapper>(), repository)

        assertThrows<IllegalStateException> {
            createCardUseCase.queryCardById(STUB_UUID)
        }
    }

    @Test
    fun `queryCardById should return card with given Id`() {
        val repository: CardRepository = mockk<CardRepository>()
        every { repository.getById(STUB_UUID) } returns CardAggregate.create(STUB_UUID)
        val createCardUseCase = CreateCardUseCase(mockk<UuidWrapper>(), repository)

        val aggregate: CardAggregate = createCardUseCase.queryCardById(STUB_UUID)

        assertEquals(STUB_UUID, aggregate.aggregateId)
    }
}
