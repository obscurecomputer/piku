package computer.obscure.piku.core.scripting.platform

import kotlinx.serialization.Serializable

interface WireRef {
    val refType: String
    val refId: String
}