package it.giannibombelli.wsc2024

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

fun assertNotBlankFromJsonElement(jsonObject: JsonObject, field: String) {
    val idElement = jsonObject[field]
    assertNotNull(idElement)
    assertTrue(idElement.jsonPrimitive.content.isNotBlank())
}

fun assertEqualsFromJsonElement(expected: String, jsonObject: JsonObject, field: String) {
    val nameElement = jsonObject[field]
    assertNotNull(nameElement, "$field must not be null")
    assertEquals(expected, nameElement.jsonPrimitive.content)
}

fun assertEqualsFromJsonElement(expected: Int, jsonObject: JsonObject, field: String) {
    val nameElement = jsonObject[field]
    assertNotNull(nameElement, "$field must not be null")
    assertEquals(expected, nameElement.jsonPrimitive.content.toInt())
}

suspend fun ApplicationTestBuilder.testClient(client: suspend (HttpClient) -> Unit) {
    client(createClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
            })
        }
    })
}
