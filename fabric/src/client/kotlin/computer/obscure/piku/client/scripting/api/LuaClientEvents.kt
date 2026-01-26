package computer.obscure.piku.client.scripting.api

import computer.obscure.piku.client.packets.serverbound.payloads.SendDataPayload
import computer.obscure.piku.client.scripting.ClientEventBus
import computer.obscure.piku.client.scripting.events.HeartbeatEvent
import computer.obscure.piku.common.scripting.base.LuaEvent
import computer.obscure.piku.common.utils.toJson
import computer.obscure.twine.nativex.conversion.Converter.toLuaValue
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import org.luaj.vm2.LuaValue

class LuaClientEvents : ClientEventBus {
    val customListeners =
        mutableMapOf<String, MutableList<(LuaValue) -> Unit>>()

    val baseListeners =
        mutableMapOf<String, LuaEvent>()

    fun registerBaseListeners() {
        register(HeartbeatEvent)
    }

    private fun register(luaEvent: LuaEvent) {
        baseListeners[luaEvent.id] = luaEvent
    }

    fun clear() {
        customListeners.clear()
        baseListeners.clear()
    }

    // lua > server
    override fun send(eventId: String, data: LuaValue) {
        val json = data.toJson()

        val payload = SendDataPayload(
            id = eventId,
            json = json
        )

        ClientPlayNetworking.send(payload)
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
}