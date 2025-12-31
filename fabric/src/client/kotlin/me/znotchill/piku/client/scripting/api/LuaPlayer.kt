package me.znotchill.piku.client.scripting.api

import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import me.znotchill.piku.common.scripting.base.Player
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