package computer.obscure.piku.mod.fabric.scripting.api.sound

import computer.obscure.piku.mod.fabric.sound.TrackManager
import computer.obscure.piku.mod.fabric.sound.TrackRegistry
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction

class LuaSound : TwineNative("sound") {

    @TwineFunction
    fun track(id: String): LuaSoundTrack {
        return LuaSoundTrack(id)
    }

    @TwineFunction("get")
    fun getTrack(id: String): LuaSoundTrack? {
        return TrackRegistry.get(id)?.let { LuaSoundTrack(it) }
    }

    @TwineFunction
    fun exists(id: String): Boolean {
        return TrackRegistry.get(id) != null
    }

    @TwineFunction
    fun stop(id: String) {
        TrackManager.stop(id)
    }

    @TwineFunction
    fun stop(id: String, fadeTicks: Int) {
        TrackManager.stop(id, fadeTicks)
    }
}