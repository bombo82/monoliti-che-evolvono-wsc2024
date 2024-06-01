package it.giannibombelli.wsc2024.card

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import it.giannibombelli.wsc2024.testClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals

class CardApiForBookModuleTest {

    @Test
    fun `bookDeposited should return 404 when card does not exist`() = testServer {
        val response = it.post("/card/f2dff8c3-217f-4c0a-984c-7deac07aeffe/book-deposited")

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `bookDeposited should return 204 when card is updated`() = testServer {
        val postResponse = it.post("/card")
        val cardId: String = postResponse.body<JsonObject>()["id"]!!.jsonPrimitive.content

        val response = it.post("/card/$cardId/book-deposited")

        assertEquals(HttpStatusCode.NoContent, response.status)
    }

    private fun testServer(client: suspend (HttpClient) -> Unit) {
        testApplication {
            install(IgnoreTrailingSlash)
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                })
            }

            application {
                cardModule()
            }

            testClient(client)
        }
    }
}
