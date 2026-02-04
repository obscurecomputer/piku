package computer.obscure.piku.mod.common

import computer.obscure.piku.mod.common.events.ClientPlayConnection
import computer.obscure.piku.mod.common.events.ClientTick
import computer.obscure.piku.mod.common.packets.clientbound.ClientPacketHandlers
import computer.obscure.piku.mod.common.packets.clientbound.payloads.ReceiveDataPayload
import computer.obscure.piku.mod.common.packets.clientbound.payloads.ReceiveScriptPayload
import computer.obscure.piku.mod.common.scripting.engine.ClientLuaEngine
import computer.obscure.piku.mod.common.ui.UIRenderer
import dev.architectury.networking.NetworkManager
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class Piku {
    companion object {
        val MOD_ID = "piku"

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

        fun init() {
            println("Mod initialised (common)!")
            initClient()
        }

        fun initClient() {
            engine.init()
            InputTracker.init()
            UIRenderer.register()
            ClientPacketHandlers.init()

            ClientPlayConnection.register()
            ClientTick.register()

            LOGGER.info("Piku Common Client Initialized")
        }
    }
}