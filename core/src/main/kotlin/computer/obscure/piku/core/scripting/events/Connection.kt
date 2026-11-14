package computer.obscure.piku.core.scripting.events

import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty

class Connection(private val onDisconnect: () -> Unit) : TwineNative() {
    @Volatile
    private var alive = true

    @TwineProperty
    val connected get() = alive

    @TwineFunction
    fun disconnect() {
        if (alive) {
            alive = false
            onDisconnect()
        }
    }
}