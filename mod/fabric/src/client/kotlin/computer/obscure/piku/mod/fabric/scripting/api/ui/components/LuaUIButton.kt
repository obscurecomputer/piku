package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.mod.fabric.ui.components.ActivateMode
import computer.obscure.piku.mod.fabric.ui.components.ButtonNode
import computer.obscure.twine.LuaCallback
import computer.obscure.twine.annotations.TwineFunction

open class LuaUIButton(override val node: ButtonNode) : LuaUIText(node) {
    @TwineFunction
    fun activateMode(value: String) = apply {
        node.activateMode = when (value) {
            "p", "press" -> ActivateMode.PRESS
            "r", "release" -> ActivateMode.RELEASE
            "h", "hover" -> ActivateMode.HOVER
            else -> ActivateMode.RELEASE
        }
    }

    @TwineFunction
    fun onActivate(value: LuaCallback) = apply {
        node.onActivate = { uiNode ->
            try {
                value.invoke(uiNode)
            } catch (_: Exception) {}
        }
    }
}