package computer.obscure.piku.mod.common.packets.clientbound.handlers

import computer.obscure.piku.core.utils.jsonStringToLua
import computer.obscure.piku.mod.common.Piku
import computer.obscure.piku.mod.common.packets.clientbound.payloads.ReceiveDataPayload
import net.minecraft.client.Minecraft

object ReceiveDataHandler {
    fun handle(payload: ReceiveDataPayload) {
        Minecraft.getInstance().execute {
            try {
                Piku.engine.events.fire(payload.id, jsonStringToLua(payload.json))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}