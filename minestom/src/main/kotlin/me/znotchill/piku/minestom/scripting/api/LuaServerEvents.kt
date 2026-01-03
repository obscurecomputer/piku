package me.znotchill.piku.minestom.scripting.api

import me.znotchill.piku.common.scripting.base.Player
import me.znotchill.piku.minestom.scripting.ServerEventBus
import org.luaj.vm2.LuaValue

class LuaServerEvents : ServerEventBus {
    private val listeners = mutableMapOf<String, MutableList<(LuaValue) -> Unit>>()

    override fun listen(eventId: String, callback: (LuaValue) -> Unit) {
        listeners.computeIfAbsent(eventId) { mutableListOf() }.add(callback)
    }

    override fun fire(eventId: String, data: LuaValue) {
        listeners[eventId]?.forEach { it(data) }
    }

    override fun send(player: Player, eventId: String, data: LuaValue) {
        println("Sending $eventId to ${player.uuid}")
    }
}