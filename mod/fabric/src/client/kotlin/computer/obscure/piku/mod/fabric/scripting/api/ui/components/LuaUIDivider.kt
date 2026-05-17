package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaUINode
import computer.obscure.piku.mod.fabric.ui.components.DividerNode
import computer.obscure.twine.annotations.TwineFunction

class LuaUIDivider(override val node: DividerNode) : LuaUINode(node) {
    @TwineFunction
    fun color(value: LuaColorInstance): LuaUIDivider {
        node.color = value.toUIColor()
        return this
    }

    @TwineFunction
    fun thickness(value: Float): LuaUIDivider {
        node.thickness = value
        return this
    }
}
