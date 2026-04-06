package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import computer.obscure.piku.core.scripting.base.Player
import net.minecraft.network.chat.Component

class LuaPlayer(
    private val player: net.minecraft.world.entity.player.Player
) : Player() {

    @TwineFunction
    override fun send(message: String) {
        player.displayClientMessage(Component.literal(message), false)
    }

    @TwineProperty
    override val uuid: String
        get() = player.uuid.toString()

    @TwineProperty
    override val username: String
        get() = player.gameProfile.name
}