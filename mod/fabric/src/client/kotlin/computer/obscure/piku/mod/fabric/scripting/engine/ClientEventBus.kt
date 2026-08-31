package computer.obscure.piku.mod.fabric.scripting.engine

import computer.obscure.piku.core.scripting.base.EventBus
import computer.obscure.piku.core.scripting.base.Event
import computer.obscure.piku.core.utils.toJson
import computer.obscure.piku.mod.fabric.packets.serverbound.SendDataPacket
import computer.obscure.piku.mod.fabric.scripting.events.BrandEvent
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import java.util.UUID

open class ClientEventBus : EventBus {
    val customListeners = mutableMapOf<String, MutableList<(Map<String, Any?>) -> Unit>>()
    val baseListeners = mutableMapOf<String, Event>()
    val stateCallbacks: MutableMap<UUID, (Map<String, Any?>) -> Unit> = mutableMapOf()

    fun registerBaseListeners() {
        register(BrandEvent)
    }

    private fun register(event: Event) {
        baseListeners[event.id] = event
    }

    fun send(eventId: String, data: Map<String, Any?>) {
        val payload = SendDataPacket(
            id = eventId,
            json = data.toJson()
        )
        ClientPlayNetworking.send(payload)
    }

    override fun listen(
        eventId: String,
        callback: (Map<String, Any?>) -> Unit
    ) {
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
        baseListeners[eventId]?.let { event ->
            try {
                event.onClientReceive(data)
            } catch (e: Exception) {
                println("[Lua error] in base event $eventId: ${e.message}")
            }
        }
    }
}