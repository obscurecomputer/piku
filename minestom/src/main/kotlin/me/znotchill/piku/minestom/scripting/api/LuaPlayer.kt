package me.znotchill.piku.minestom.scripting.api

import me.znotchill.piku.common.scripting.base.Player
import net.kyori.adventure.text.Component

class LuaPlayer(val player: net.minestom.server.entity.Player) : Player {
    override fun send(message: String) {
        player.sendMessage(Component.text(message))
    }

    override val uuid: String
        get() = player.uuid.toString()
}
