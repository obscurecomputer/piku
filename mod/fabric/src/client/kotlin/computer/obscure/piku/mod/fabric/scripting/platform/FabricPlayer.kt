package computer.obscure.piku.mod.fabric.scripting.platform

import computer.obscure.piku.core.scripting.platform.CorePlayer
import computer.obscure.piku.mod.fabric.utils.toNativeComponent
import net.kyori.adventure.text.Component
import net.minecraft.world.entity.player.Player

class FabricPlayer(
    val player: Player
) : FabricEntity(player), CorePlayer {
    override fun send(message: Component) {
        player.sendSystemMessage(message.toNativeComponent())
    }
}