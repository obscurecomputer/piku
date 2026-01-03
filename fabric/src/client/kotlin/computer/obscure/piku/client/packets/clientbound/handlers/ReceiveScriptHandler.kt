package computer.obscure.piku.client.packets.clientbound.handlers

import computer.obscure.piku.client.PikuClient
import computer.obscure.piku.client.packets.clientbound.payloads.ReceiveScriptPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

class ReceiveScriptHandler {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(ReceiveScriptPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                println("Received script ${payload.name} from the server!")
                PikuClient.engine.runScript(payload.name, payload.fileContents)
            }
        }
    }
}