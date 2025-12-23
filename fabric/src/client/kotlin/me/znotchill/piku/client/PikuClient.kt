package me.znotchill.piku.client

import me.znotchill.piku.client.packets.clientbound.handlers.ReceiveScriptHandler
import me.znotchill.piku.client.packets.clientbound.payloads.ReceiveScriptPayload
import me.znotchill.piku.client.packets.serverbound.payloads.SendDataPayload
import me.znotchill.piku.client.scripting.FabricLuaEngine
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry

class PikuClient : ClientModInitializer {
    companion object {
        val engine = FabricLuaEngine()
    }

    override fun onInitializeClient() {
        engine.init()
        InputTracker.init()

        PayloadTypeRegistry.playS2C().register(ReceiveScriptPayload.ID, ReceiveScriptPayload.CODEC)
        ReceiveScriptHandler().register()

        PayloadTypeRegistry.playC2S().register(SendDataPayload.ID, SendDataPayload.CODEC)

        ClientPlayConnectionEvents.DISCONNECT.register { _, _ ->
            engine.events.listeners.clear()
        }
    }
}
