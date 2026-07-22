package computer.obscure.piku.mod.fabric.sound

import net.minecraft.client.sounds.SoundManager

class ActiveTrack(private val def: TrackDef) {
    private val channels = mutableMapOf<String, LayerChannel>()
    private val layerVolumes = mutableMapOf<String, Float>()
    private var startedAtMs: Long = 0
    private var loopLengthMs: Long = 0

    fun start(soundManager: SoundManager) {
        startedAtMs = System.currentTimeMillis()
        for (layer in def.layers) {
            layerVolumes[layer.id] = layer.defaultVolume
            if (layer.defaultVolume > 0f) {
                startLayer(soundManager, layer)
            }
        }
    }

    private fun startLayer(soundManager: SoundManager, layer: LayerDef) {
        val channel = LayerChannel(layer.sound, def.category, layerVolumes[layer.id] ?: 1f, layer.id)
        channels[layer.id] = channel
        soundManager.play(channel)
    }

    fun setLayerVolume(trackSoundManager: SoundManager, layerId: String, target: Float, fadeTicks: Int) {
        layerVolumes[layerId] = target
        val existing = channels[layerId]

        if (existing == null && target > 0f) {
            // layer wasn't playing, needs to start now
            val layerDef = def.layers.find { it.id == layerId } ?: return
            startLayer(trackSoundManager, layerDef)
            channels[layerId]?.fadeTo(target, fadeTicks)
        } else if (existing != null && target <= 0f) {
            // fade out then actually stop
            existing.fadeTo(0f, fadeTicks)
            existing.scheduleStopAfterFade(fadeTicks) { trackSoundManager.stop(existing); channels.remove(layerId) }
        } else {
            existing?.fadeTo(target, fadeTicks)
        }
    }

    fun stop(soundManager: SoundManager, fadeTicks: Int = 0) {
        if (fadeTicks <= 0) {
            channels.values.forEach { soundManager.stop(it) }
            channels.clear()
        } else {
            channels.values.forEach { it.fadeTo(0f, fadeTicks) }
        }
    }

    fun tickLayers() {
        channels.values.forEach {
            it.advanceFade()
        }
    }

    fun allLayerIds(): Set<String> = def.layers.map { it.id }.toSet()
}