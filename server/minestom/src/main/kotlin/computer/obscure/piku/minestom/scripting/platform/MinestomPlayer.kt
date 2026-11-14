package computer.obscure.piku.minestom.scripting.platform

import net.kyori.adventure.text.Component
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.Player
import computer.obscure.piku.core.scripting.platform.Player as CorePlayer

class MinestomPlayer(
    val player: Player
) : MinestomEntity(player), CorePlayer<EntityType> {
    override fun send(component: Component) {
        player.sendMessage(component)
    }
}