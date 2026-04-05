package computer.obscure.piku.mod.fabric

import computer.obscure.piku.mod.fabric.events.ClientPlayConnection
import computer.obscure.piku.mod.fabric.events.ClientTick
import computer.obscure.piku.mod.fabric.packets.clientbound.handlers.ReceiveDataHandler
import computer.obscure.piku.mod.fabric.packets.clientbound.handlers.ReceiveScriptHandler
import computer.obscure.piku.mod.fabric.packets.clientbound.handlers.ReceiveStateHandler
import computer.obscure.piku.mod.fabric.packets.clientbound.payloads.ReceiveDataPayload
import computer.obscure.piku.mod.fabric.packets.clientbound.payloads.ReceiveScriptPayload
import computer.obscure.piku.mod.fabric.packets.clientbound.payloads.ReceiveStatePayload
import computer.obscure.piku.mod.fabric.packets.serverbound.payloads.SendDataPayload
import computer.obscure.piku.mod.fabric.packets.serverbound.payloads.SendStatePayload
import computer.obscure.piku.mod.fabric.scripting.engine.ClientLuaEngine
import computer.obscure.piku.mod.fabric.ui.UIRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class PikuClient : ClientModInitializer {
    companion object {
        const val MOD_ID = "piku"

        val engine = ClientLuaEngine()

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
        InputHandler.init()
        UIRenderer.register()

        PayloadTypeRegistry.playS2C().register(ReceiveScriptPayload.TYPE, ReceiveScriptPayload.STREAM_CODEC)
        PayloadTypeRegistry.playS2C().register(ReceiveDataPayload.TYPE, ReceiveDataPayload.STREAM_CODEC)
        PayloadTypeRegistry.playS2C().register(ReceiveStatePayload.TYPE, ReceiveStatePayload.STREAM_CODEC)
        PayloadTypeRegistry.playC2S().register(SendDataPayload.TYPE, SendDataPayload.CODEC)
        PayloadTypeRegistry.playC2S().register(SendStatePayload.TYPE, SendStatePayload.CODEC)

        ClientPlayNetworking.registerGlobalReceiver(ReceiveScriptPayload.TYPE) { payload, context ->
            ReceiveScriptHandler.handle(payload)
        }

        ClientPlayNetworking.registerGlobalReceiver(ReceiveDataPayload.TYPE) { payload, context ->
            ReceiveDataHandler.handle(payload)
        }

        ClientPlayNetworking.registerGlobalReceiver(ReceiveStatePayload.TYPE) { payload, context ->
            ReceiveStateHandler.handle(payload)
        }
        ClientPlayConnection.register()
        ClientTick.register()

        LOGGER.info("Piku Client Initialized")
    }
}