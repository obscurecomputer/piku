package computer.obscure.piku.minestom.scripting.platform

import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import computer.obscure.piku.core.scripting.platform.CorePlayer
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineProperty
import net.minestom.server.coordinate.Pos
import java.util.UUID
import computer.obscure.piku.core.classes.Pos as CorePos
import me.znotchill.kiwi.generated.Vec3 as CoreVec3

class MinestomPlayer(
    val player: Player
) : MinestomEntity(player), CorePlayer {
    override fun send(message: Component) {
        player.sendMessage(message)
    }
}