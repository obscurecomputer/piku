package computer.obscure.piku.mod.common.events

import computer.obscure.piku.core.scheduler.Scheduler
import computer.obscure.piku.mod.common.Client
import computer.obscure.piku.mod.common.Piku
import computer.obscure.piku.mod.common.ui.UIRenderer
import dev.architectury.event.events.client.ClientPlayerEvent

object ClientPlayConnection {
    fun register() {
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register {
            onJoin()
        }

        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register {
            onDisconnect()
        }
    }

    fun onJoin() {
        Client.connectedToServer = true
        Client.serverRunsPiku = true // TODO: change this
        Piku.engine.init()
    }

    fun onDisconnect() {
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
            customScreenshotInstance = null
        }

        Piku.engine.shutdown()

        // !!! VERY IMPORTANT
        // This clears all active scripts upon disconnect
        Piku.engine.activeScripts.clear()
        UIRenderer.reset()
        Scheduler.reset()
    }
}