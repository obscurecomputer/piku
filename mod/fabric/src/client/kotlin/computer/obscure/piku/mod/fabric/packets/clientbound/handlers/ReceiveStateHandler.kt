package computer.obscure.piku.mod.fabric.packets.clientbound.handlers

import computer.obscure.piku.core.scripting.server.SharedStateManager
import computer.obscure.piku.core.utils.jsonStringToKotlin
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.packets.clientbound.payloads.ReceiveStatePayload
import computer.obscure.piku.mod.fabric.scripting.api.LuaSharedState
import net.minecraft.client.Minecraft

object ReceiveStateHandler {
    fun handle(payload: ReceiveStatePayload) {
        Minecraft.getInstance().execute {
            try {
                val value = jsonStringToKotlin(payload.value)
                val luaState = LuaSharedState(
                    internalId = payload.internalId,
                    name = payload.name,
                    value = value,
                    clientModifiable = payload.clientModifiable
                )

                val state = luaState.toSharedState()

                PikuClient.engine!!.events.stateCallbacks[state.internalId]?.invoke(
                    mapOf("value" to payload.value)
                )

                SharedStateManager.addState(state)
                PikuClient.engine!!.events.fire(
                    "client.update_state",
                    mapOf(
                        "internalId" to luaState.internalId,
                        "name" to luaState.name,
                        "value" to luaState.value,
                        "clientModifiable" to luaState.clientModifiable
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}