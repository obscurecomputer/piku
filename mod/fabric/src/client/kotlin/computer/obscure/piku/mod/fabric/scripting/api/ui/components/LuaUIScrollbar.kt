package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaUINode
import computer.obscure.piku.mod.fabric.ui.components.ScrollbarDirection
import computer.obscure.piku.mod.fabric.ui.components.ScrollbarNode
import computer.obscure.twine.annotations.TwineFunction
import me.znotchill.kiwi.generated.Color

class LuaUIScrollbar(override val node: ScrollbarNode) : LuaUINode(node) {
    @TwineFunction
    fun target(value: LuaUIFlow): LuaUIScrollbar {
        node.target = value.node
        return this
    }

    @TwineFunction
    fun trackColor(value: Color): LuaUIScrollbar {
        node.trackColor = value
        return this
    }

    @TwineFunction
    fun thumbColor(value: Color): LuaUIScrollbar {
        node.thumbColor = value
        return this
    }

    @TwineFunction
    fun direction(value: String): LuaUIScrollbar {
        node.direction = when (value.lowercase()) {
            "horizontal", "h" -> ScrollbarDirection.HORIZONTAL
            else -> ScrollbarDirection.VERTICAL
        }
        return this
    }
}
