package computer.obscure.piku.minestom.scripting.api

import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import computer.obscure.piku.core.scripting.base.Player
import net.kyori.adventure.text.minimessage.MiniMessage

class LuaPlayer(
    private val player: net.minestom.server.entity.Player
) : Player() {

    private val mm = MiniMessage.miniMessage()

    @TwineFunction
    override fun send(message: String) {
        player.sendMessage(mm.deserialize(message))
    }

    @TwineProperty
    override val uuid: String
        get() = player.uuid.toString()

    @TwineProperty
    override val username: String
        get() = player.username
}