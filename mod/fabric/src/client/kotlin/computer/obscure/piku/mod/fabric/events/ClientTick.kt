package computer.obscure.piku.mod.fabric.events

import computer.obscure.piku.core.scheduler.Scheduler
import computer.obscure.piku.mod.fabric.Client
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.camera.CameraAnimator
import computer.obscure.piku.mod.fabric.ui.UIRenderer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents

object ClientTick {
    fun register() {
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            if (PikuClient.engine?.twine?.closed == true) {
                PikuClient.error("Engine closed! Reopening.")
                ClientPlayConnection.onDisconnect()
                if (Client.connectedToServer) {
                    PikuClient.engine!!.init()
                }
            } else {
                Scheduler.tick()
            }

            if (client.level == null) return@register
            UIRenderer.handleUpdateRender()
            CameraAnimator.tick(1.0 / 20.0)

            // do NOT override if hideHUD is false,
            // it stops F1 working entirely
            if (Client.hideHUD)
                client.options.hideGui = true

        }
    }
}