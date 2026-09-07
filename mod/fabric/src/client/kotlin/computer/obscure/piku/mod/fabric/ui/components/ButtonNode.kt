package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaUINode
import computer.obscure.piku.mod.fabric.ui.classes.UIEvent

open class ButtonNode : TextNode() {
    var activateMode: ActivateMode = ActivateMode.RELEASE

    var onActivate: (LuaUINode) -> Unit = {}

    open fun onBaseActivate(node: LuaUINode) {
        onActivate(node)
    }

    override fun onBaseHover(event: UIEvent, node: LuaUINode) {
        if (activateMode == ActivateMode.HOVER) {
            onBaseActivate(node)
        }
        super.onBaseHover(event, node)
    }

    override fun onBasePress(event: UIEvent, node: LuaUINode) {
        if (activateMode == ActivateMode.PRESS) {
            onBaseActivate(node)
        }
        super.onBasePress(event, node)
    }

    override fun onBaseRelease(event: UIEvent, node: LuaUINode) {
        if (activateMode == ActivateMode.RELEASE) {
            onBaseActivate(node)
        }
        super.onBaseRelease(event, node)
    }
}

enum class ActivateMode {
    PRESS,
    HOVER,
    RELEASE
}