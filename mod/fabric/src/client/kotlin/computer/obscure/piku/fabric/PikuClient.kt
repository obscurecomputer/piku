package computer.obscure.piku.fabric

import computer.obscure.piku.fabric.events.ClientPlayConnection
import computer.obscure.piku.fabric.events.ClientTick
import computer.obscure.piku.fabric.packets.clientbound.handlers.ReceiveDataHandler
import computer.obscure.piku.fabric.packets.clientbound.handlers.ReceiveScriptHandler
import computer.obscure.piku.fabric.packets.clientbound.payloads.ReceiveDataPayload
import computer.obscure.piku.fabric.packets.clientbound.payloads.ReceiveScriptPayload
import computer.obscure.piku.fabric.packets.serverbound.payloads.SendDataPayload
import computer.obscure.piku.fabric.scripting.engine.FabricLuaEngine
import computer.obscure.piku.fabric.ui.UIRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
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
        PayloadTypeRegistry.playS2C().register(ReceiveDataPayload.ID, ReceiveDataPayload.CODEC)
        ReceiveDataHandler().register()

        PayloadTypeRegistry.playC2S().register(SendDataPayload.ID, SendDataPayload.CODEC)

        ClientPlayConnection.register()
        ClientTick.register()
    }
}
