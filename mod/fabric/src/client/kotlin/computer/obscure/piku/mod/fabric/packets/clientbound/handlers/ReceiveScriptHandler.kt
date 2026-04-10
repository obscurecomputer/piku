package computer.obscure.piku.mod.fabric.packets.clientbound.handlers

import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.packets.clientbound.payloads.ReceiveScriptPayload
import net.minecraft.client.Minecraft

object ReceiveScriptHandler {
    fun handle(payload: ReceiveScriptPayload) {
        val engineRef = PikuClient.engine
        Minecraft.getInstance().execute {
            try {
                if (PikuClient.engine !== engineRef) return@execute
                if (payload.name == "END_OF_SCRIPT_LOADING") {
                    PikuClient.LOGGER.debug("Server sent EOSL")
                    engineRef!!.activeScripts.forEach { script ->
                        engineRef.runScript(script.key, script.value)
                    }
                    return@execute
                }
                PikuClient.engine!!.activeScripts[payload.name] = payload.fileContents
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}