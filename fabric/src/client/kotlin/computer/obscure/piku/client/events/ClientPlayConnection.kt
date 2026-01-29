package computer.obscure.piku.client.events

import computer.obscure.piku.client.Client
import computer.obscure.piku.client.PikuClient.Companion.engine
import computer.obscure.piku.client.ui.UIRenderer
import computer.obscure.piku.common.scheduler.Scheduler
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler

object ClientPlayConnection {
    fun register() {
        ClientPlayConnectionEvents.DISCONNECT.register { networkHandler, client ->
            onDisconnect(networkHandler, client)
        }
        ClientPlayConnectionEvents.JOIN.register { networkHandler, packetSender, client ->
            onJoin(networkHandler, packetSender, client)
        }
    }

    fun onJoin(
        networkHandler: ClientPlayNetworkHandler,
        packetSender: PacketSender,
        client: MinecraftClient
    ) {
        Client.connectedToServer = true
        Client.serverRunsPiku = true // TODO: change this
        engine.init()
    }

    fun onDisconnect(
        networkHandler: ClientPlayNetworkHandler,
        client: MinecraftClient
    ) {
        Client.customScreenshotMessage = null
        Client.customScreenshotInstance = null
        Client.connectedToServer = false
        Client.serverRunsPiku = false

        engine.shutdown()

        // !!! VERY IMPORTANT
        // This clears all active scripts upon disconnect
        engine.activeScripts.clear()
        UIRenderer.reset()
        Scheduler.reset()
    }
}