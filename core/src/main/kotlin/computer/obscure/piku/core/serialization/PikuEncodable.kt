package computer.obscure.piku.core.serialization

import kotlinx.serialization.json.JsonElement

interface PikuEncodable {
    fun encodeToJson(): JsonElement
}