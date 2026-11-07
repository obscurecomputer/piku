package computer.obscure.piku.core.serialization

import kotlinx.serialization.KSerializer

interface PikuEncodable {
    fun serializer(): KSerializer<*>
}