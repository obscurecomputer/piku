package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.mod.fabric.scripting.api.util.Axis
import computer.obscure.piku.mod.fabric.ui.components.RowNode
import computer.obscure.twine.annotations.TwineFunction

class LuaUIRow(override val node: RowNode) : LuaUIContainer(node) {
    @TwineFunction
    fun gap(value: Double): LuaUIRow {
        node.gap = value.toFloat()
        return this
    }

    @TwineFunction
    fun mainAxis(value: String): LuaUIRow {
        node.mainAxis = Axis.parseMainAxis(value)
        return this
    }

    @TwineFunction
    fun crossAxis(value: String): LuaUIRow {
        node.crossAxis = Axis.parseCrossAxis(value)
        return this
    }
}