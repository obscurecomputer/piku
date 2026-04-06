package computer.obscure.piku.mod.fabric.packets.clientbound.handlers

import computer.obscure.piku.core.utils.jsonStringToKotlin
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.packets.clientbound.payloads.ReceiveDataPayload
import net.minecraft.client.Minecraft

object ReceiveDataHandler {
    fun handle(payload: ReceiveDataPayload) {
        Minecraft.getInstance().execute {
            try {
                val data = jsonStringToKotlin(payload.json) as? Map<String, Any?> ?: emptyMap()
                PikuClient.engine.events.fire(payload.id, data)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}