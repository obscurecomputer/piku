package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaVec2Instance
import computer.obscure.piku.mod.fabric.ui.components.LineNode
import computer.obscure.twine.annotations.TwineFunction

class LuaUILine(override val node: LineNode) : LuaUIContainer(node) {
    @TwineFunction
    fun to(value: LuaVec2Instance): LuaUILine {
        node.to = value.toVec2()
        return this
    }
    @TwineFunction
    fun thickness(value: Int): LuaUILine {
        node.thickness = value
        return this
    }
    @TwineFunction
    fun fixedOrigin(value: Boolean): LuaUILine {
        node.fixedOrigin = value
        return this
    }
}