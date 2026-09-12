package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.ui.classes.UIEvent
import computer.obscure.piku.mod.fabric.ui.components.TextInputNode
import computer.obscure.twine.LuaCallback
import computer.obscure.twine.annotations.TwineFunction

class LuaUITextInput(override val node: TextInputNode) : LuaUIText(node) {
    @TwineFunction
    fun focus() = apply {
        val menu = PikuClient.uiMenu() ?: return@apply
        menu.focusedNode = node
        node.focused = true
        node.onBaseFocus(
            UIEvent.Focus(
                screenX = 0f,
                screenY = 0f,
                localX = 0f,
                localY = 0f,
                node = this,
                buttonIndex = 0
            ),
            this
        )
    }

    @TwineFunction
    fun focus(value: Boolean) = apply {
        val menu = PikuClient.uiMenu() ?: return@apply

        node.focused = value
        if (value) {
            menu.focusedNode = node
            focus()
            return@apply
        }
        menu.focusedNode = null
        node.onBaseUnfocus(
            UIEvent.FocusDropped,
            this
        )
    }

    @TwineFunction
    fun placeholder(value: String): LuaUITextInput {
        val component = PikuClient.miniMessage
            .deserialize(value)
        node.placeholderComponent = component
        node.placeholder = value
        return this
    }

    @TwineFunction
    override fun text(value: Any): LuaUIText {
        PikuClient.uiMenu() ?: return this
        val editBox = node.editBox ?: return this
        editBox.value = value.toString()
        return super.text(value)
    }

    @TwineFunction
    fun onInput(value: LuaCallback) = apply {
        node.onInput = { event, uiNode ->
            try {
                value.invoke(event, uiNode)
            } catch (_: Exception) {}
        }
    }

    @TwineFunction
    fun onConfirm(value: LuaCallback) = apply {
        node.onConfirm = { event, uiNode ->
            try {
                value.invoke(event, uiNode)
            } catch (_: Exception) {}
        }
    }
}