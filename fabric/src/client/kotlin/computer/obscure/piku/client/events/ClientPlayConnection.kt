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
        Client.customPitch = 0f
        Client.customYaw = 0f
        Client.customRoll = 0f
        Client.targetFov = -1f
        Client.lockFov = false
        Client.isInterpolatingFov = false
        Client.fovAnimTicks = 5
        Client.animateFov = false
        Client.previousFov = -1f
        Client.fovTicksRemaining = 0

        Client.cameraOffsetX = 0f
        Client.cameraOffsetY = 0f
        Client.cameraOffsetZ = 0f

        Client.connectedToServer = false
        Client.serverRunsPiku = false
        Client.cameraLocked = false
        Client.mouseButtonsLocked = false
        Client.emitMouseEvents = false
        Client.isLeftClicking = false
        Client.isRightClicking = false
        Client.perspectiveLocked = false
        Client.hideHUD = false
        Client.hideHotbar = false
        Client.hideArm = false

        Client.customScreenshotMessage = null
        Client.customScreenshotInstance = null

        engine.shutdown()

        // !!! VERY IMPORTANT
        // This clears all active scripts upon disconnect
        engine.activeScripts.clear()
        UIRenderer.reset()
        Scheduler.reset()
    }
}