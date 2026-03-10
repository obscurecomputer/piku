package computer.obscure.piku.mod.common.scripting.api

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.piku.core.scripting.base.Player
import net.minecraft.network.chat.Component

class LuaPlayer(
    private val player: net.minecraft.world.entity.player.Player
) : Player() {

    @TwineNativeFunction
    override fun send(message: String) {
        player.displayClientMessage(Component.literal(message), false)
    }

    @TwineNativeProperty
    override val uuid: String
        get() = player.uuid.toString()

    @TwineNativeProperty
    override val username: String
        get() = player.gameProfile.name
}