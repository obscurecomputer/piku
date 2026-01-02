package me.znotchill.piku.minestom.scripting.api

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import me.znotchill.piku.common.scripting.base.Player
import net.kyori.adventure.text.minimessage.MiniMessage

class LuaPlayer(
    private val player: net.minestom.server.entity.Player
) : Player() {

    private val mm = MiniMessage.miniMessage()

    @TwineNativeFunction
    override fun send(message: String) {
        player.sendMessage(mm.deserialize(message))
    }

    @TwineNativeProperty
    override val uuid: String
        get() = player.uuid.toString()

    @TwineNativeProperty
    override val username: String
        get() = player.username
}