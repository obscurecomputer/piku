package computer.obscure.piku.minestom.scripting.api

import computer.obscure.piku.minestom.scripting.ServerEventBus
import net.minestom.server.entity.Player
import org.luaj.vm2.LuaValue


class LuaServerEvents : ServerEventBus {
    private val listeners = mutableMapOf<String, MutableList<(LuaValue, Player) -> Unit>>()

    fun listen(eventId: String, callback: (LuaValue, Player) -> Unit) {
        listeners.computeIfAbsent(eventId) { mutableListOf() }.add(callback)
    }

    fun fire(eventId: String, data: LuaValue, player: Player) {
        listeners[eventId]?.forEach { it(data, player) }
    }

    override fun listen(eventId: String, callback: (LuaValue) -> Unit) {
        listen(eventId) { data, _ -> callback(data) }
    }

    override fun fire(eventId: String, data: LuaValue) {
        // fire cannot really work here, since the data *has* to be sent to a valid player
    }

    override fun send(
        player: computer.obscure.piku.common.scripting.base.Player,
        eventId: String,
        data: LuaValue
    ) {}
}