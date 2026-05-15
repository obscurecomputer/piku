package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.piku.mod.fabric.ui.components.ProgressBarNode
import computer.obscure.twine.annotations.TwineFunction

class LuaUIProgressBar(override val node: ProgressBarNode) : LuaUIContainer(node) {
    @TwineFunction
    fun value(v: Float): LuaUIProgressBar {
        node.value = v
        return this
    }

    @TwineFunction
    fun foreground(value: LuaColorInstance): LuaUIProgressBar {
        node.foreground = value.toUIColor()
        return this
    }

    @TwineFunction
    fun track(value: LuaColorInstance): LuaUIProgressBar {
        node.track = value.toUIColor()
        return this
    }
}
