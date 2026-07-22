package computer.obscure.piku.mod.fabric.sound

import computer.obscure.piku.core.service.PikuService

object TrackRegistry : PikuService {
    private val tracks = mutableMapOf<String, TrackDef>()

    override fun shutdown() {
        tracks.clear()
    }

    fun register(track: TrackDef) {
        tracks[track.id] = track
    }

    fun get(id: String): TrackDef? = tracks[id]
}