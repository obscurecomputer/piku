package computer.obscure.piku.minestom.scripting.api

import computer.obscure.piku.minestom.scripting.ServerEventBus
import org.luaj.vm2.LuaValue

class LuaServerEvents : ServerEventBus {
    private val listeners = mutableMapOf<String, MutableList<(LuaValue) -> Unit>>()

    override fun listen(eventId: String, callback: (LuaValue) -> Unit) {
        listeners.computeIfAbsent(eventId) { mutableListOf() }.add(callback)
    }

    override fun fire(eventId: String, data: LuaValue) {
        listeners[eventId]?.forEach { it(data) }
    }

    override fun send(
        player: computer.obscure.piku.common.scripting.base.Player,
        eventId: String,
        data: LuaValue
    ) {}
}