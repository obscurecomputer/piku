package computer.obscure.piku.core.scripting.platform

import computer.obscure.piku.core.scripting.api.LuaTextInstance
import computer.obscure.twine.annotations.TwineFunction
import net.kyori.adventure.text.Component

interface CorePlayer<T> : CoreEntity<T> {
    override val refType: String
        get() = "player"

    @TwineFunction
    fun send(component: Component)

    @TwineFunction
    fun send(text: LuaTextInstance) {
        send(text.toComponent())
    }
    @TwineFunction
    fun send(value: Any) {
        send(Component.text(value.toString()))
    }
}