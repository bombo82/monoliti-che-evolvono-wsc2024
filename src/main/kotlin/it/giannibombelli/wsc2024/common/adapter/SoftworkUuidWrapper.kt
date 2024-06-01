package it.giannibombelli.wsc2024.common.adapter

import it.giannibombelli.wsc2024.common.domain.UuidWrapper
import it.giannibombelli.wsc2024.common.domain.UUID as UUUIDWrapper
import kotlinx.uuid.UUID as KotlinxUuidUUID

open class SoftworkUuidWrapper : UuidWrapper {

    override fun generateUUID(): UUUIDWrapper = KotlinxUuidUUID().toString()

    override fun isValid(uuidString: String) = KotlinxUuidUUID.isValidUUIDString(uuidString)

    override fun isNotValid(uuidString: String) = !KotlinxUuidUUID.isValidUUIDString(uuidString)

}
