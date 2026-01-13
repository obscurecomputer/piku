package computer.obscure.piku.client

import computer.obscure.piku.client.camera.CameraAnimator
import computer.obscure.piku.client.packets.clientbound.handlers.ReceiveScriptHandler
import computer.obscure.piku.client.packets.clientbound.payloads.ReceiveScriptPayload
import computer.obscure.piku.client.packets.serverbound.payloads.SendDataPayload
import computer.obscure.piku.client.scripting.engine.FabricLuaEngine
import computer.obscure.piku.client.ui.UIRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.client.MinecraftClient
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class PikuClient : ClientModInitializer {
    companion object {
        val engine = FabricLuaEngine()
        val LOGGER: Logger = LogManager.getLogger()

        fun warn(message: Any) {
            LOGGER.warn(message)
        }

        fun error(message: Any) {
            LOGGER.error(message)
        }

        fun info(message: Any) {
            LOGGER.info(message)
        }
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

        ClientTickEvents.END_CLIENT_TICK.register {
            CameraAnimator.tick(1.0 / 20.0)
            MinecraftClient.getInstance().options.hudHidden = Client.hideHUD
        }

        ClientPlayConnectionEvents.JOIN.register { _, _, _ ->
            Client.connectedToServer = true
            Client.serverRunsPiku = true // TODO: change this
            engine.init()
        }
    }
}
