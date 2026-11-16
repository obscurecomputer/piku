package computer.obscure.piku.core.scripting.platform

import computer.obscure.twine.annotations.TwineProperty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.znotchill.kiwi.blossom.serializer.KeySerializer
import net.kyori.adventure.key.Key

interface CoreBlock : DtoConvertible {
    @TwineProperty val key: Key
    @TwineProperty val properties: Map<String, String>

    override fun toDto() = CoreBlockDto(
        key = key,
        properties = properties,
    )
}

@Serializable @SerialName("block")
data class CoreBlockDto(
    @Serializable(with = KeySerializer::class)
    val key: Key,
    val properties: Map<String, String> = mutableMapOf()
) : Dto {
    override fun fromDto() = Platform.current.block(this)
}