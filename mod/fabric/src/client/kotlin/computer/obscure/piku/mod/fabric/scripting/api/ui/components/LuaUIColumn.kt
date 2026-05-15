package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.mod.fabric.scripting.api.util.Axis
import computer.obscure.piku.mod.fabric.ui.components.ColumnNode
import computer.obscure.twine.annotations.TwineFunction

class LuaUIColumn(override val node: ColumnNode) : LuaUIContainer(node) {
    @TwineFunction
    fun gap(value: Double): LuaUIColumn {
        node.gap = value.toFloat()
        return this
    }

    @TwineFunction
    fun mainAxis(value: String): LuaUIColumn {
        node.mainAxis = Axis.parseMainAxis(value)
        return this
    }

    @TwineFunction
    fun crossAxis(value: String): LuaUIColumn {
        node.crossAxis = Axis.parseCrossAxis(value)
        return this
    }
}