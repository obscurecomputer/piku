package computer.obscure.piku.mod.fabric.events

import computer.obscure.piku.core.scheduler.Scheduler
import computer.obscure.piku.mod.fabric.sound.TrackManager
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents

object ClientTick {
    fun register() {
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            Scheduler.tick()

            if (client.level == null) return@register

            TrackManager.tick()
        }
    }
}