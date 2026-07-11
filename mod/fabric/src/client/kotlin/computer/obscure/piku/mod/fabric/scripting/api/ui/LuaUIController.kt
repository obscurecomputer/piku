package computer.obscure.piku.mod.fabric.scripting.api.ui

import computer.obscure.piku.mod.fabric.ui.classes.ControllerActivateMode
import computer.obscure.piku.mod.fabric.ui.classes.ControllerEdgeMode
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
    fun allowMultiActivate(value: Boolean) = apply {
        node.controllerOptions.activateMode = ControllerActivateMode.MULTI
    }
    @TwineFunction
    fun toggleActivate(value: Boolean) = apply {
        node.controllerOptions.activateMode = ControllerActivateMode.TOGGLE
    }
    @TwineFunction
    fun allowSingleToggleActivate(value: Boolean) = apply {
        node.controllerOptions.activateMode = ControllerActivateMode.SINGLE_TOGGLE
    }
    @TwineFunction
    fun scrollAxis(value: String) = apply {
        node.controllerOptions.scrollAxis = when (value.lowercase()) {
            "h", "horizontal", "horiz" -> ControllerScrollAxis.HORIZONTAL
            "v", "vertical", "vert" -> ControllerScrollAxis.VERTICAL
            else -> ControllerScrollAxis.VERTICAL
        }
    }
    @TwineFunction
    fun edgeMode(value: String) = apply {
        node.controllerOptions.edgeMode = when (value.lowercase()) {
            "w", "wrap" -> ControllerEdgeMode.WRAP
            "r", "rn", "require_nudge" -> ControllerEdgeMode.REQUIRE_NUDGE
            "s", "stop" -> ControllerEdgeMode.STOP
            else -> ControllerEdgeMode.STOP
        }
    }
}