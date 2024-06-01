package it.giannibombelli.wsc2024.book

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import it.giannibombelli.wsc2024.Environment
import it.giannibombelli.wsc2024.assertEqualsFromJsonElement
import it.giannibombelli.wsc2024.assertNotBlankFromJsonElement
import it.giannibombelli.wsc2024.testClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BookApiTest {

    @Test
    fun `return 201 as HTTP status when successfully POST a new book`() = testServer {
        val response = it.post("/book") {
            contentType(ContentType.Application.Json)
            setBody(
                """
                    {
                        "cardId": "f2dff8c3-217f-4c0a-984c-7deac07aeffe",   
                        "name": "Beyond Legacy Code",
                        "author": "David Scott Bernstein"
                    }
                """.trimIndent()
            )
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `return Location header with the resource location when successfully POST a new card`() = testServer {
        val (protocol, fqdn, port) = Environment().http
        val response = it.post("/book") {
            contentType(ContentType.Application.Json)
            setBody(
                """
                    {
                        "cardId": "f2dff8c3-217f-4c0a-984c-7deac07aeffe",   
                        "name": "Beyond Legacy Code",
                        "author": "David Scott Bernstein"
                    }
                """.trimIndent()
            )
        }

        assertNotNull(response.headers["Location"])
        assertTrue((response.headers["Location"] as String).startsWith("$protocol://$fqdn:$port/book/"))
    }

    @Test
    fun `return body with the created resource when successfully POST a new book`() = testServer {
        val response = it.post("/book") {
            contentType(ContentType.Application.Json)
            setBody(
                """
                    {
                        "cardId": "f2dff8c3-217f-4c0a-984c-7deac07aeffe",   
                        "name": "Beyond Legacy Code",
                        "author": "David Scott Bernstein"
                    }
                """.trimIndent()
            )
        }

        val jsonObject: JsonObject = response.body<JsonObject>()
        assertNotNull(jsonObject)
        assertNotBlankFromJsonElement(jsonObject, "id")
        assertEqualsFromJsonElement("Beyond Legacy Code", jsonObject, "name")
        assertEqualsFromJsonElement("David Scott Bernstein", jsonObject, "author")
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
                bookModule(FakeCardClient())
            }

            testClient(client)
        }
    }
}
