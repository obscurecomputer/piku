package computer.obscure.piku.mod.fabric.events

import computer.obscure.piku.core.scheduler.Scheduler
import computer.obscure.piku.core.scripting.server.SharedStateManager
import computer.obscure.piku.mod.fabric.Client
import computer.obscure.piku.mod.fabric.InputHandler
import computer.obscure.piku.mod.fabric.MenuConfigs
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.packets.clientbound.handlers.ReceiveScriptHandler
import computer.obscure.piku.mod.fabric.ui.UIRenderer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents

object ClientPlayConnection {
    fun register() {
        ClientPlayConnectionEvents.JOIN.register { _, _, client ->
            client.execute {
                onJoin()
            }
        }

        ClientPlayConnectionEvents.DISCONNECT.register { _, client ->
            client.execute {
                onDisconnect()
            }
        }
    }

    fun onJoin() {
        Client.connectedToServer = true
        Client.serverRunsPiku = true // TODO: change this
        PikuClient.engine.init()
    }

    fun onDisconnect() {
        SharedStateManager.shutdown()
        PikuClient.engine.shutdown()
        UIRenderer.shutdown()
        Scheduler.shutdown()
        InputHandler.clearInputQueue()

        Client.apply {
            customPitch = 0f
            customYaw = 0f
            customRoll = 0f
            targetFov = -1f
            lockFov = false
            isInterpolatingFov = false
            fovAnimTicks = 5
            animateFov = false
            previousFov = -1f
            fovTicksRemaining = 0

            cameraOffsetX = 0f
            cameraOffsetY = 0f
            cameraOffsetZ = 0f

            connectedToServer = false
            serverRunsPiku = false
            cameraLocked = false
            mouseButtonsLocked = false
            emitMouseEvents = false
            isLeftClicking = false
            isRightClicking = false
            perspectiveLocked = false
            hideHUD = false
            hideHotbar = false
            hideArm = false

            customScreenshotMessage = null
            bobbingStrength = 1f

            menuConfigs = MenuConfigs()
        }
    }
}