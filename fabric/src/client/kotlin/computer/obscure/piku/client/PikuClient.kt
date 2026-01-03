package computer.obscure.piku.client

import computer.obscure.piku.client.packets.clientbound.handlers.ReceiveScriptHandler
import computer.obscure.piku.client.packets.clientbound.payloads.ReceiveScriptPayload
import computer.obscure.piku.client.packets.serverbound.payloads.SendDataPayload
import computer.obscure.piku.client.scripting.FabricLuaEngine
import computer.obscure.piku.client.ui.UIRenderer
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

        UIRenderer.register()

        PayloadTypeRegistry.playS2C().register(ReceiveScriptPayload.ID, ReceiveScriptPayload.CODEC)
        ReceiveScriptHandler().register()

        PayloadTypeRegistry.playC2S().register(SendDataPayload.ID, SendDataPayload.CODEC)

        ClientPlayConnectionEvents.DISCONNECT.register { _, _ ->
            UIRenderer.currentWindow.components.clear()
            engine.shutdown()
            Client.connectedToServer = false
            Client.serverRunsPiku = false
        }

//        ClientPlayerEntity.

        ClientPlayConnectionEvents.JOIN.register { _, _, _ ->
            Client.connectedToServer = true
            Client.serverRunsPiku = true // TODO: change this
            engine.init()
        }
    }
}
