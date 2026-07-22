package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.mod.fabric.ui.components.GradientNode
import computer.obscure.twine.annotations.TwineFunction
import me.znotchill.kiwi.generated.Color

class LuaUIGradient(override val node: GradientNode) : LuaUIContainer(node) {
    @TwineFunction
    fun from(value: Color): LuaUIGradient {
        node.colorStart = value
        return this
    }

    @TwineFunction
    fun to(value: Color): LuaUIGradient {
        node.colorEnd = value
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