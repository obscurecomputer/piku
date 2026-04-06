package computer.obscure.piku.minestom.scripting.api

import computer.obscure.piku.minestom.scripting.ServerEventBus
import net.minestom.server.entity.Player

class LuaServerEvents : ServerEventBus {
    private val listeners = mutableMapOf<String, MutableList<(Map<String, Any?>, Player) -> Unit>>()

    fun fire(eventId: String, data: Map<String, Any?>, player: Player) {
        listeners[eventId]?.forEach { it(data, player) }
    }

    override fun listen(eventId: String, callback: (Map<String, Any?>) -> Unit) {
        listeners.computeIfAbsent(eventId) { mutableListOf() }.add { data, _ ->
            callback(data)
        }
    }


    fun listen(eventId: String, callback: (Map<String, Any?>, Player) -> Unit) {
        listeners.computeIfAbsent(eventId) { mutableListOf() }.add { data, player ->
            callback(data, player)
        }
    }

    override fun fire(eventId: String, data: Map<String, Any?>) {
    }

    override fun send(
        player: computer.obscure.piku.core.scripting.base.Player,
        eventId: String,
        data: Any?
    ) {}
}