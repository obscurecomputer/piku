package computer.obscure.piku.mod.fabric.scripting.api.ui

import computer.obscure.piku.mod.fabric.ui.classes.ControllerScrollAxis
import computer.obscure.piku.mod.fabric.ui.components.FlowNode
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction

class LuaUIController(val node: FlowNode) : TwineNative() {
    @TwineFunction
    fun active(value: Boolean) = apply {
        node.controllerOptions.active = value
    }
    @TwineFunction
    fun initialDelay(value: Int) = apply {
        node.controllerOptions.initialDelay = value
    }
    @TwineFunction
    fun repeatDelay(value: Int) = apply {
        node.controllerOptions.repeatDelay = value
    }
    @TwineFunction
    fun engageThreshold(value: Float) = apply {
        node.controllerOptions.engageThreshold = value
    }
    @TwineFunction
    fun releaseThreshold(value: Float) = apply {
        node.controllerOptions.releaseThreshold = value
    }
    @TwineFunction
    fun scrollAxis(value: String) = apply {
        node.controllerOptions.scrollAxis = when (value.lowercase()) {
            "h", "horizontal", "horiz" -> ControllerScrollAxis.HORIZONTAL
            "v", "vertical", "vert" -> ControllerScrollAxis.VERTICAL
            else -> ControllerScrollAxis.VERTICAL
        }
    }
}