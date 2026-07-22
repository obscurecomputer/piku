package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import me.znotchill.kiwi.generated.Vec2
import computer.obscure.piku.mod.fabric.ui.components.LineNode
import computer.obscure.twine.annotations.TwineFunction

class LuaUILine(override val node: LineNode) : LuaUIContainer(node) {
    @TwineFunction
    fun to(value: Vec2): LuaUILine {
        node.to = value
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