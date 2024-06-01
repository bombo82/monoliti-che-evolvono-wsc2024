package it.giannibombelli.wsc2024

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

val basePath = "${Environment().http.protocol}://${Environment().http.fqdn}:${Environment().http.port}"

/**
 * This file contains acceptance tests written from the viewpoint of the user of our system and covers end-to-end flow.
 * To run these tests we need to launch the application manually, and we could run them test either against a local
 * instance or a remote ones.
 *
 * PAY ATTENTION: These tests already cover the feature to be developed and must NOT be modified in any way!
 * They currently fail just because the feature is not yet finished and this failing test could give us information
 * about the development status.
 */
class CustomerTest {

    @Test
    fun `Can emit a new card`() {
        lateinit var httpStatusCode: HttpStatusCode

        When {
            `Ask for a new card`().let {
                httpStatusCode = it.status
            }
        }
        Then {
            `A new card is created`(httpStatusCode)
        }
    }

    @Test
    fun `Gain one point when deposit a book`() {
        var cardId: String? = null

        Given {
            `Ask for a new card`().let {
                runBlocking {
                    val jsonElement = it.body<JsonObject>()["id"]
                    requireNotNull(jsonElement)
                    cardId = jsonElement.jsonPrimitive.content
                }
            }
        }
        When {
            `Deposit a book`(cardId, "eXtreme Programming eXplained", "Kent Beck")
        }
        Then {
            `Card gain point`(cardId, 1)
        }
    }

    private fun `Ask for a new card`(): HttpResponse = runBlocking {
        val httpResponse = testHttpClient().post("$basePath/card")

        assertEquals(HttpStatusCode.Created, httpResponse.status)

        httpResponse
    }

    private fun `A new card is created`(httpStatusCode: HttpStatusCode) {
        assertEquals(HttpStatusCode.Created, httpStatusCode)
    }

    private fun `Deposit a book`(cardId: String?, name: String, author: String): HttpResponse = runBlocking {
        requireNotNull(cardId)
        require(cardId.isNotBlank())

        val httpResponse = testHttpClient().post("$basePath/book") {
            contentType(ContentType.Application.Json)
            setBody(
                """
                    {
                        "cardId": "$cardId",
                        "name": "$name",
                        "author": "$author"
                    }
                """.trimIndent()
            )
        }

        assertEquals(HttpStatusCode.Created, httpResponse.status)

        httpResponse
    }

    private fun `Card gain point`(cardId: String?, expectedBalance: Int): HttpResponse = runBlocking {
        requireNotNull(cardId)
        require(cardId.isNotBlank())

        val httpResponse = testHttpClient().get("$basePath/card/$cardId")

        assertEquals(HttpStatusCode.OK, httpResponse.status)
        val jsonObject: JsonObject = httpResponse.body<JsonObject>()
        assertNotNull(jsonObject)
        assertEqualsFromJsonElement(expectedBalance, jsonObject, "balance")

        httpResponse
    }

    private fun testHttpClient(): HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
            })
        }
    }
}

private inline fun Given(f: () -> Unit): Unit = f()
private inline fun When(f: () -> Unit): Unit = f()
private inline fun Then(f: () -> Unit): Unit = f()
