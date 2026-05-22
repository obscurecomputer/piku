package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaUINode
import computer.obscure.piku.mod.fabric.ui.components.DividerNode
import computer.obscure.twine.annotations.TwineFunction

class LuaUIDivider(override val node: DividerNode) : LuaUINode(node) {
    @TwineFunction
    fun thickness(value: Float): LuaUIDivider {
        node.thickness = value
        return this
    }
}
