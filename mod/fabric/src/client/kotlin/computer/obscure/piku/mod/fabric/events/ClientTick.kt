package computer.obscure.piku.mod.fabric.events

import computer.obscure.piku.core.scheduler.Scheduler
import computer.obscure.piku.mod.fabric.Client
import computer.obscure.piku.mod.fabric.camera.CameraAnimator
import computer.obscure.piku.mod.fabric.ui.UIRenderer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents

object ClientTick {
    fun register() {
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            UIRenderer.handleUpdateRender()
            CameraAnimator.tick(1.0 / 20.0)

            if (client.level == null) return@register

            // do NOT override if hideHUD is false,
            // it stops F1 working entirely
            if (Client.hideHUD)
                client.options.hideGui = true

            Scheduler.tick()
        }
    }
}