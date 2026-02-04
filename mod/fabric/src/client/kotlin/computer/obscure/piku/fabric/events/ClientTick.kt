package computer.obscure.piku.fabric.events

import computer.obscure.piku.fabric.Client
import computer.obscure.piku.fabric.camera.CameraAnimator
import computer.obscure.piku.common.scheduler.Scheduler
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient

object ClientTick {
    fun register() {
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            CameraAnimator.tick(1.0 / 20.0)

            if (client.world == null) return@register
            MinecraftClient.getInstance().options.hudHidden = Client.hideHUD

            Scheduler.tick()
        }
    }
}