package computer.obscure.piku.core.scripting.platform

import computer.obscure.piku.core.classes.Pos
import computer.obscure.twine.annotations.TwineProperty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.znotchill.kiwi.blossom.serializer.UUIDSerializer
import java.util.*

interface CoreEntity : DtoConvertible {
    val uuid: UUID
    @TwineProperty
    var position: Pos

    override fun toDto(): Dto =
        CoreEntityDto(uuid, position)
}

@Serializable @SerialName("entity")
data class CoreEntityDto(
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID,
    val position: Pos
) : Dto {
    override fun fromDto() = Platform.current.entityFromDto(this)
}
