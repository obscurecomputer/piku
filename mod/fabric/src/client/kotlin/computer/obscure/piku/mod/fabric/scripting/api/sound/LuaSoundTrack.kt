package computer.obscure.piku.mod.fabric.scripting.api.sound

import computer.obscure.piku.mod.fabric.sound.LayerDef
import computer.obscure.piku.mod.fabric.sound.TrackDef
import computer.obscure.piku.mod.fabric.sound.TrackManager
import computer.obscure.piku.mod.fabric.sound.TrackRegistry
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundSource

class LuaSoundTrack : TwineNative {

    private val trackId: String
    private val layers = mutableMapOf<String, LayerDef>()
    private var loop: Boolean = true
    private var category: SoundSource = SoundSource.MUSIC

    constructor(id: String) : super() {
        trackId = id
    }

    constructor(existing: TrackDef) : super() {
        trackId = existing.id
        existing.layers.forEach {
            layers[it.id] = it
        }
        loop = existing.loop
        category = existing.category
    }

    @TwineProperty
    val id: String
        get() = trackId

    @TwineFunction
    fun layer(id: String, sound: String, defaultVolume: Float = 0f): LuaSoundTrack = apply {
        layers[id] = LayerDef(id, Identifier.parse(sound), defaultVolume)
    }

    @TwineFunction
    fun loop(value: Boolean): LuaSoundTrack = apply {
        loop = value
    }

    @TwineFunction
    fun category(value: String): LuaSoundTrack = apply {
        category = runCatching {
            SoundSource.valueOf(value.uppercase())
        }
            .getOrDefault(SoundSource.MUSIC)
    }

    @TwineFunction
    fun register(): LuaSoundTrack = apply {
        TrackRegistry.register(TrackDef(trackId, layers.values.toList(), loop, category))
    }

    @TwineFunction
    fun play(): LuaSoundTrack = apply {
        register()
        TrackManager.play(trackId)
    }

    @TwineFunction
    fun stop(): LuaSoundTrack = apply {
        TrackManager.stop(trackId)
    }

    @TwineFunction
    fun stop(fadeTicks: Int): LuaSoundTrack = apply {
        TrackManager.stop(trackId, fadeTicks)
    }

    @TwineFunction
    fun setLayer(layerId: String, volume: Float = 1f, fadeTicks: Int = 10): LuaSoundTrack = apply {
        println("[TEST] LuaSoundTrack.setLayer called: trackId=$trackId layerId=$layerId volume=$volume fadeTicks=$fadeTicks")
        TrackManager.setLayer(trackId, layerId, volume, fadeTicks)
    }
}