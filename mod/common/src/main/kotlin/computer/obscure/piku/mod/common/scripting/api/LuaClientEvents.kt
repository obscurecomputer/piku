package computer.obscure.piku.mod.common.scripting.api

import computer.obscure.piku.core.scripting.api.LuaEventData
import computer.obscure.piku.core.scripting.base.LuaEvent
import computer.obscure.piku.core.states.SharedState
import computer.obscure.piku.core.utils.toJson
import computer.obscure.piku.mod.common.packets.serverbound.payloads.SendDataPayload
import computer.obscure.piku.mod.common.packets.serverbound.payloads.SendStatePayload
import computer.obscure.piku.mod.common.scripting.ClientEventBus
import computer.obscure.piku.mod.common.scripting.events.HeartbeatEvent
import computer.obscure.twine.nativex.conversion.Converter.toLuaValue
import dev.architectury.networking.NetworkManager
import org.luaj.vm2.LuaValue
import java.util.UUID

class LuaClientEvents : ClientEventBus {
    val customListeners =
        mutableMapOf<String, MutableList<(LuaValue) -> Unit>>()

    val baseListeners =
        mutableMapOf<String, LuaEvent>()

    val stateCallbacks: MutableMap<UUID, LuaValue> = mutableMapOf()

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

    // lua > server
    override fun send(eventId: String, data: LuaValue) {
        val json = data.toJson()

        val payload = SendDataPayload(
            id = eventId,
            json = json
        )

        NetworkManager.sendToServer(payload)
    }

    fun send(eventId: String, data: Any) {
        send(eventId, data.toLuaValue())
    }

    // lua > local register
    override fun listen(eventId: String, callback: (LuaValue) -> Unit) {
        customListeners.computeIfAbsent(eventId) { mutableListOf() }
            .add(callback)
    }

    // minecraft > lua
    override fun fire(eventId: String, data: LuaValue) {
        customListeners[eventId]?.forEach { callback ->
            try {
                callback(data)
            } catch (e: Exception) {
                println("[Lua error] in event $eventId: ${e.message}")
            }
        }
    }

    fun fire(eventId: String, data: LuaEventData) {
        fire(eventId, data.table)
    }

    fun sendState(state: SharedState) {
        val payload = SendStatePayload(
            internalId = state.internalId.toString(),
            value = state.value.toLuaValue().toJson()
        )

        NetworkManager.sendToServer(payload)
    }
}