package computer.obscure.piku.mod.common

import computer.obscure.piku.mod.common.events.ClientPlayConnection
import computer.obscure.piku.mod.common.events.ClientTick
import computer.obscure.piku.mod.common.packets.serverbound.payloads.SendDataPayload
import computer.obscure.piku.mod.common.scripting.engine.ClientLuaEngine
import computer.obscure.piku.mod.common.ui.UIRenderer
import dev.architectury.networking.NetworkManager
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class Piku {
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

        fun init() {
            println("Mod initialised (common)!")
            initClient()
        }

        fun initClient() {
            engine.init()
            InputTracker.init()
            UIRenderer.register()

            NetworkManager.registerReceiver(
                NetworkManager.clientToServer(),
                SendDataPayload.TYPE,
                SendDataPayload.CODEC
            ) { _, _ ->
                // Do nothing, since this is C2S
            }

            ClientPlayConnection.register()
            ClientTick.register()

            LOGGER.info("Piku Common Client Initialized")
        }
    }
}