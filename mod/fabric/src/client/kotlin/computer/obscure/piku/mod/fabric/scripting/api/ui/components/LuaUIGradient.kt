package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.piku.mod.fabric.ui.components.GradientNode
import computer.obscure.twine.annotations.TwineFunction

class LuaUIGradient(override val node: GradientNode) : LuaUIContainer(node) {
    @TwineFunction
    fun from(value: LuaColorInstance): LuaUIGradient {
        node.colorStart = value.toUIColor()
        return this
    }

    @TwineFunction
    fun to(value: LuaColorInstance): LuaUIGradient {
        node.colorEnd = value.toUIColor()
        return this
    }

    @TwineFunction
    fun direction(value: String): LuaUIGradient {
        node.direction = when (value) {
            "h", "horizontal" -> GradientNode.GradientDirection.HORIZONTAL
            "v", "vertical" -> GradientNode.GradientDirection.VERTICAL
            else -> GradientNode.GradientDirection.HORIZONTAL
        }
        return this
    }
}