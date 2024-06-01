package it.giannibombelli.wsc2024.card

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import it.giannibombelli.wsc2024.card.domain.CardAggregate
import it.giannibombelli.wsc2024.card.domain.CardRepository
import it.giannibombelli.wsc2024.card.domain.QueryService
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

private const val STUB_UUID = "f2dff8c3-217f-4c0a-984c-7deac07aeffe"

@ExtendWith(MockKExtension::class)
@MockKExtension.CheckUnnecessaryStub
class QueryServiceTest {

    @Test
    fun `queryCardById should return null when not found a card`() {
        val repository: CardRepository = mockk<CardRepository>()
        every { repository.getById(STUB_UUID) } returns null
        val queryService = QueryService(repository)

        val card = queryService.queryCardById(STUB_UUID)

        assertNull(card)
    }

    @Test
    fun `queryCardById should return a card when card with given Id exist`() {
        val repository: CardRepository = mockk<CardRepository>()
        every { repository.getById(STUB_UUID) } returns CardAggregate.create(STUB_UUID)
        val queryService = QueryService(repository)

        val card = queryService.queryCardById(STUB_UUID)

        assertNotNull(card)
        assertEquals(STUB_UUID, card.aggregateId)
    }
}
