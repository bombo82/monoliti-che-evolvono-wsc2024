package it.giannibombelli.wsc2024.common.domain

typealias UUID = String

interface UuidWrapper {

    fun generateUUID(): UUID

    fun isValid(uuidString: String): Boolean

    fun isNotValid(uuidString: String): Boolean
}
