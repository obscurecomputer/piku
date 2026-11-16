package computer.obscure.piku.core.scripting.platform

import computer.obscure.piku.core.classes.Pos
import computer.obscure.twine.annotations.TwineFunction
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.znotchill.kiwi.blossom.serializer.UUIDSerializer
import net.kyori.adventure.text.Component
import java.util.UUID

interface CorePlayer : CoreEntity {
    fun send(message: Component) {}

    @TwineFunction
    fun send(message: String) {
        send(Component.text(message))
    }
    @TwineFunction
    fun send(message: Any) {
        send(message.toString())
    }

    @TwineFunction
    fun gameMode(gameMode: String) {}

    override fun toDto(): Dto =
        CorePlayerDto(uuid, position)
}

@Serializable @SerialName("player")
data class CorePlayerDto(
    @Serializable(with = UUIDSerializer::class) val uuid: UUID,
    val position: Pos,
) : Dto {
    override fun fromDto() = Platform.current.player(this)
}