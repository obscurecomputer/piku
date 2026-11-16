package computer.obscure.piku.core.scripting.platform

import kotlinx.serialization.Serializable

@Serializable
sealed interface Dto {
    fun fromDto(): Any?
}
interface DtoConvertible {
    fun toDto(): Dto
}
