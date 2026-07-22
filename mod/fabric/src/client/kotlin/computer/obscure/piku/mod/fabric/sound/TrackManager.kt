package computer.obscure.piku.mod.fabric.sound

import computer.obscure.piku.core.service.PikuService
import net.minecraft.client.Minecraft

object TrackManager : PikuService {
    private val active = mutableMapOf<String, ActiveTrack>()
    private val pendingStop = mutableMapOf<String, Int>()

    override fun shutdown() {
        active.toList().forEach { (id, active) ->
            active.stop(Minecraft.getInstance().soundManager, 0)
        }
        active.clear()
        pendingStop.clear()
    }

    fun play(trackId: String) {
        if (active.containsKey(trackId)) return
        val def = TrackRegistry.get(trackId) ?: return
        val track = ActiveTrack(def)
        track.start(Minecraft.getInstance().soundManager)
        active[trackId] = track
    }

    fun setLayer(trackId: String, layerId: String, volume: Float, fadeTicks: Int) {
        val track = active[trackId]
        track?.setLayerVolume(Minecraft.getInstance().soundManager, layerId, volume, fadeTicks)
    }

    fun stop(trackId: String, fadeTicks: Int = 0) {
        val track = active[trackId] ?: return
        if (fadeTicks <= 0) {
            track.stop(Minecraft.getInstance().soundManager)
            active.remove(trackId)
        } else {
            track.stop(Minecraft.getInstance().soundManager, fadeTicks)
            pendingStop[trackId] = fadeTicks
        }
    }

    fun tick() {
        active.values.forEach { it.tickLayers() }

        val iterator = pendingStop.entries.iterator()
        while (iterator.hasNext()) {
            val (trackId, ticksLeft) = iterator.next()
            if (ticksLeft <= 0) {
                active.remove(trackId)?.stop(Minecraft.getInstance().soundManager)
                iterator.remove()
            } else {
                pendingStop[trackId] = ticksLeft - 1
            }
        }
    }
}