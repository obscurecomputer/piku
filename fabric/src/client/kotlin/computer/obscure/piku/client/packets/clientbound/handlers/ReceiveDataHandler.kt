package computer.obscure.piku.client.packets.clientbound.handlers

import computer.obscure.piku.client.PikuClient
import computer.obscure.piku.client.packets.clientbound.payloads.ReceiveDataPayload
import computer.obscure.piku.common.utils.jsonStringToLua
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

class ReceiveDataHandler {
    fun register() {
        ClientPlayNetworking.registerGlobalReceiver(ReceiveDataPayload.ID) { payload, context ->
            val client = context.client()
            client.execute {
                try {
//                    println("Received data ${payload.id} with content ${payload.json}")

                    PikuClient.engine.events.fire(payload.id, jsonStringToLua(payload.json))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}