package computer.obscure.piku.mod.fabric.packets.clientbound.handlers

import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.packets.clientbound.payloads.ReceiveScriptPayload
import net.minecraft.client.Minecraft

object ReceiveScriptHandler {
    fun handle(payload: ReceiveScriptPayload) {
        Minecraft.getInstance().execute {
            try {
                if (payload.name == "END_OF_SCRIPT_LOADING") {
                    PikuClient.LOGGER.debug("Server sent EOSL")
                    PikuClient.engine.activeScripts.forEach { script ->
                        PikuClient.engine.runScript(script.key, script.value)
                    }
                    return@execute
                }
                PikuClient.engine.activeScripts[payload.name] = payload.fileContents
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}