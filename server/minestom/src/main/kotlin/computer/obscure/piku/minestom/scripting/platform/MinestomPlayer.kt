package computer.obscure.piku.minestom.scripting.platform

import computer.obscure.piku.core.scripting.platform.CorePlayer
import computer.obscure.piku.minestom.scripting.api.instance.LuaInstance
import computer.obscure.twine.annotations.TwineFunction
import net.kyori.adventure.text.Component
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player

class MinestomPlayer(
    val player: Player
) : MinestomEntity(player), CorePlayer {
    override fun send(message: Component) {
        player.sendMessage(message)
    }

    @TwineFunction
    override fun gameMode(gameMode: String) {
        try {
            player.gameMode = GameMode.valueOf(gameMode.uppercase())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @TwineFunction
    fun instance(instance: LuaInstance) {
        player.instance = instance.instance
    }
}