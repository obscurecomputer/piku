package computer.obscure.piku.mod.common.events

import computer.obscure.piku.core.scheduler.Scheduler
import computer.obscure.piku.mod.common.Client
import computer.obscure.piku.mod.common.camera.CameraAnimator
import dev.architectury.event.events.client.ClientTickEvent

object ClientTick {
    fun register() {
        ClientTickEvent.CLIENT_POST.register { client ->
            CameraAnimator.tick(1.0 / 20.0)

            if (client.level == null) return@register
            client.options.hideGui = Client.hideHUD

            Scheduler.tick()
        }
    }
}