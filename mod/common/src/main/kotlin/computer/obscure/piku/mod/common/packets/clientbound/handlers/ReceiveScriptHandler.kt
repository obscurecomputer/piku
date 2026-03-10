package computer.obscure.piku.mod.common.packets.clientbound.handlers

import computer.obscure.piku.mod.common.Piku
import computer.obscure.piku.mod.common.packets.clientbound.payloads.ReceiveScriptPayload
import net.minecraft.client.Minecraft

object ReceiveScriptHandler {
    fun handle(payload: ReceiveScriptPayload) {
        Minecraft.getInstance().execute {
            try {
                if (payload.name == "END_OF_SCRIPT_LOADING") {
                    Piku.LOGGER.debug("Server sent EOSL")
                    Piku.engine.activeScripts.forEach { script ->
                        Piku.engine.runScript(script.key, script.value)
                    }
                    return@execute
                }
                Piku.engine.activeScripts[payload.name] = payload.fileContents
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}