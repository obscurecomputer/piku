package computer.obscure.piku.mod.fabric.packets.clientbound.handlers

import computer.obscure.piku.mod.fabric.Client
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.events.ClientPlayConnection
import computer.obscure.piku.mod.fabric.packets.clientbound.payloads.UnloadScriptsPayload
import computer.obscure.piku.mod.fabric.packets.serverbound.payloads.SendUnloadedPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.Minecraft

object UnloadScriptsHandler {
    fun handle(payload: UnloadScriptsPayload) {
        Minecraft.getInstance().execute {
            try {
                ClientPlayConnection.onDisconnect {
                    Client.connectedToServer = true
                    PikuClient.engine!!.init()
                    ClientPlayNetworking.send(SendUnloadedPayload())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}