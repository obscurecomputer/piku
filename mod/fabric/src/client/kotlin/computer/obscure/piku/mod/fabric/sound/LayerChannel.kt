package computer.obscure.piku.mod.fabric.sound

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource

class LayerChannel(
    private val soundId: Identifier,
    private val category: SoundSource,
    initialVolume: Float,
    private val layerId: String = soundId.toString()
) : AbstractTickableSoundInstance(
    resolveSoundEvent(soundId), category, RandomSource.create()
) {

    @Volatile
    var currentVolume: Float = initialVolume
        private set

    private var targetVolume: Float = initialVolume
    private var fadeStep: Float = 0f
    private var fadeTicksRemaining: Int = 0
    private var onFadeOutComplete: (() -> Unit)? = null

    init {
        this.looping = true
        this.delay = 0
        this.volume = initialVolume
        this.attenuation = SoundInstance.Attenuation.NONE
        this.relative = true
    }

    fun fadeTo(target: Float, ticks: Int) {
        if (ticks <= 0) {
            currentVolume = target
            fadeTicksRemaining = 0
            return
        }
        targetVolume = target
        fadeStep = (target - currentVolume) / ticks
        fadeTicksRemaining = ticks
    }

    fun scheduleStopAfterFade(ticks: Int, callback: () -> Unit) {
        onFadeOutComplete = callback
    }

    fun advanceFade() {
        if (fadeTicksRemaining > 0) {
            currentVolume += fadeStep
            fadeTicksRemaining--
            if (fadeTicksRemaining == 0) {
                currentVolume = targetVolume
                if (currentVolume <= 0.001f) {
                    onFadeOutComplete?.invoke()
                    onFadeOutComplete = null
                }
            }
        }
    }

    override fun tick() {
    }

    override fun getVolume() = currentVolume
    override fun isStopped(): Boolean = false

    companion object {
        private fun resolveSoundEvent(id: Identifier): SoundEvent {
            val holder = BuiltInRegistries.SOUND_EVENT.get(id)
            return holder
                .map { it.value() }
                .orElseGet { SoundEvent.createVariableRangeEvent(id) }
        }
    }
}