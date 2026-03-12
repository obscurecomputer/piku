package computer.obscure.piku.mod.common.packets.clientbound.handlers

import computer.obscure.piku.core.scripting.server.SharedStateManager
import computer.obscure.piku.core.utils.jsonStringToLua
import computer.obscure.piku.mod.common.Piku
import computer.obscure.piku.mod.common.packets.clientbound.payloads.ReceiveStatePayload
import computer.obscure.piku.mod.common.scripting.api.LuaSharedState
import net.minecraft.client.Minecraft

object ReceiveStateHandler {
    fun handle(payload: ReceiveStatePayload) {
        Minecraft.getInstance().execute {
            try {
                val luaState = LuaSharedState(
                    internalId = payload.internalId,
                    name = payload.name,
                    value = jsonStringToLua(
                        payload.value
                    ),
                    clientModifiable = payload.clientModifiable
                )
                SharedStateManager.addState(luaState.toSharedState())
                Piku.engine.events.fire(
                    "client.update_state",
                    luaState
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}