package computer.obscure.piku.fabric.scripting.api

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.piku.common.scripting.base.Player
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text


class LuaPlayer(
    private val player: PlayerEntity
) : Player() {

    @TwineNativeFunction
    override fun send(message: String) {
        player.sendMessage(Text.literal(message), false)
    }

    @TwineNativeProperty
    override val uuid: String
        get() = player.uuid.toString()

    @TwineNativeProperty
    override val username: String
        get() = player.gameProfile.name
}