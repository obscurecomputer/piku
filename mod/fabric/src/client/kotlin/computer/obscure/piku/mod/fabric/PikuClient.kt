package computer.obscure.piku.mod.fabric

import computer.obscure.piku.mod.fabric.events.ClientHudRender
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
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.kyori.adventure.text.minimessage.MiniMessage
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.system.exitProcess

class PikuClient : ClientModInitializer {
    companion object {
        val LOGGER: Logger = LogManager.getLogger()

        val miniMessage = MiniMessage.miniMessage()
        const val MOD_ID = "piku"

        var engine: ClientLuaEngine? = null
            private set

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
        try {
            info("[PIKU] Instantiating Lua Engine...")
            engine = ClientLuaEngine()
            engine?.init()
            info("[PIKU] Engine started successfully.")
        } catch (e: Throwable) {
            error("[PIKU] Engine failed to start!")
            e.printStackTrace()
            exitProcess(1)
        }

        LOGGER.info("Piku Client Initialized successfully!")
        InputHandler.init()

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
        ClientHudRender.register()
    }
}