package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.mod.fabric.ui.components.ProgressBarNode
import computer.obscure.twine.annotations.TwineFunction
import me.znotchill.kiwi.generated.Color

class LuaUIProgressBar(override val node: ProgressBarNode) : LuaUIContainer(node) {
    @TwineFunction
    fun value(v: Float): LuaUIProgressBar {
        node.value = v
        return this
    }

    @TwineFunction
    fun foreground(value: Color): LuaUIProgressBar {
        node.foreground = value
        return this
    }

    @TwineFunction
    fun track(value: Color): LuaUIProgressBar {
        node.track = value
        return this
    }
}
