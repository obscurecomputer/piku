package computer.obscure.piku.mod.common.packets.clientbound

import computer.obscure.piku.core.utils.jsonStringToLua
import computer.obscure.piku.mod.common.Piku
import computer.obscure.piku.mod.common.packets.clientbound.payloads.ReceiveDataPayload
import computer.obscure.piku.mod.common.packets.clientbound.payloads.ReceiveScriptPayload
import dev.architectury.networking.NetworkManager

object ClientPacketHandlers {
    fun init() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C,
            ReceiveDataPayload.TYPE, ReceiveDataPayload.STREAM_CODEC) { payload, context ->
            context.queue {
                try {
                    Piku.engine.events.fire(payload.id, jsonStringToLua(payload.json))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        NetworkManager.registerReceiver(NetworkManager.Side.S2C,
            ReceiveScriptPayload.TYPE, ReceiveScriptPayload.STREAM_CODEC) { payload, context ->
            context.queue {
                try {
                    val engine = Piku.engine
                    if (payload.name == "END_OF_SCRIPT_LOADING") {
                        engine.activeScripts.forEach { (name, content) ->
                            engine.runScript(name, content)
                        }
                    } else {
                        engine.activeScripts[payload.name] = payload.fileContents
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}