package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.piku.core.scripting.api.LuaEventData
import computer.obscure.piku.core.scripting.base.LuaEvent
import computer.obscure.piku.core.states.SharedState
import computer.obscure.piku.core.utils.toJson
import computer.obscure.piku.mod.fabric.packets.serverbound.payloads.SendDataPayload
import computer.obscure.piku.mod.fabric.packets.serverbound.payloads.SendStatePayload
import computer.obscure.piku.mod.fabric.scripting.ClientEventBus
import computer.obscure.piku.mod.fabric.scripting.events.HeartbeatEvent
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import java.util.UUID

class LuaClientEvents : ClientEventBus {
    val customListeners = mutableMapOf<String, MutableList<(Map<String, Any?>) -> Unit>>()
    val baseListeners = mutableMapOf<String, LuaEvent>()
    val stateCallbacks: MutableMap<UUID, (Map<String, Any?>) -> Unit> = mutableMapOf()

    fun registerBaseListeners() {
        register(HeartbeatEvent)
    }

    private fun register(luaEvent: LuaEvent) {
        baseListeners[luaEvent.id] = luaEvent
    }

    fun clear() {
        customListeners.clear()
        baseListeners.clear()
        stateCallbacks.clear()
    }

    override fun send(eventId: String, data: Map<String, Any?>) {
        val payload = SendDataPayload(
            id = eventId,
            json = data.toJson()
        )
        ClientPlayNetworking.send(payload)
    }

    override fun listen(eventId: String, callback: (Map<String, Any?>) -> Unit) {
        customListeners.computeIfAbsent(eventId) { mutableListOf() }.add(callback)
    }

    override fun fire(eventId: String, data: Map<String, Any?>) {
        customListeners[eventId]?.forEach { callback ->
            try {
                callback.invoke(data)
            } catch (e: Exception) {
                println("[Lua error] in event $eventId: ${e.message}")
            }
        }
    }

    fun sendState(state: SharedState) {
        val payload = SendStatePayload(
            internalId = state.internalId.toString(),
            value = state.value.toJson()
        )
        ClientPlayNetworking.send(payload)
    }
}