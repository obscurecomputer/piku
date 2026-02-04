package computer.obscure.piku.fabric.packets.clientbound.handlers

import computer.obscure.piku.fabric.PikuClient.Companion.engine
import computer.obscure.piku.fabric.packets.clientbound.payloads.ReceiveScriptPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

class ReceiveScriptHandler {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(ReceiveScriptPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                try {
                    if (payload.name == "END_OF_SCRIPT_LOADING") {
                        engine.activeScripts.forEach { script ->
                            engine.runScript(script.key, script.value)
                        }
                        return@execute
                    }
                    engine.activeScripts[payload.name] = payload.fileContents
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}