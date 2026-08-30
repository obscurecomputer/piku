package computer.obscure.piku.mod.fabric.scripting.old.ui.components

import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.ui.components.TextInputNode
import computer.obscure.piku.mod.fabric.utils.toNativeComponent
import computer.obscure.twine.annotations.TwineFunction

class LuaUITextInput(override val node: TextInputNode) : LuaUIText(node) {
    @TwineFunction
    fun placeholder(value: String): LuaUITextInput {
        val component = PikuClient.miniMessage
            .deserialize(value)
        node.placeholderComponent = component.toNativeComponent()
        node.placeholder = value
        return this
    }
}