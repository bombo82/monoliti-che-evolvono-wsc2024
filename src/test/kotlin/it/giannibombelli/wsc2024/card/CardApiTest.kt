package it.giannibombelli.wsc2024.card

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import it.giannibombelli.wsc2024.Environment
import it.giannibombelli.wsc2024.assertNotBlankFromJsonElement
import it.giannibombelli.wsc2024.testClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CardApiTest {

    @Test
    fun `return 201 as HTTP status when successfully POST a new card`() = testServer {
        val response = it.post("/card")

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `return Location header with the resource location when successfully POST a new card`() = testServer {
        val (protocol, fqdn, port) = Environment().http
        val response = it.post("/card")

        assertNotNull(response.headers["Location"])
        assertTrue((response.headers["Location"] as String).startsWith("$protocol://$fqdn:$port/card/"))
    }

    @Test
    fun `return body with the created resource when successfully POST a new card`() = testServer {
        val response = it.post("/card")

        val jsonObject: JsonObject = response.body<JsonObject>()
        assertNotNull(jsonObject)
        assertNotBlankFromJsonElement(jsonObject, "id")
    }

    @Test
    fun `return 200 as HTTP status code when card is present`() = testServer {
        val postResponse = it.post("/card")
        val cardId: String = postResponse.body<JsonObject>()["id"]!!.jsonPrimitive.content

        val response = it.get("/card/$cardId")

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `return 404 as HTTP status code when card is NOT present`() = testServer {
        val response = it.get("/card/uuid")

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `return body with the requested card when it is present`() = testServer {
        val postResponse = it.post("/card")
        val cardId: String = postResponse.body<JsonObject>()["id"]!!.jsonPrimitive.content

        val response = it.get("/card/$cardId")

        val jsonObject: JsonObject = response.body<JsonObject>()
        assertNotNull(jsonObject)
        assertNotBlankFromJsonElement(jsonObject, "id")
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
