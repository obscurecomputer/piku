package me.znotchill.piku.client.scripting.api

import me.znotchill.piku.common.scripting.base.Player
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text

class LuaPlayer(val player: PlayerEntity) : Player {
    override fun send(message: String) {
        player.sendMessage(Text.literal(message), false)
    }

    override val uuid: String
        get() = player.uuid.toString()
}