package computer.obscure.piku.mod.fabric.ui.classes

import computer.obscure.piku.mod.fabric.controlify.ActionEvent
import computer.obscure.piku.mod.fabric.controlify.BindingEvent
import computer.obscure.piku.mod.fabric.controlify.ControllerBind

data class ControllerFlowOptions(
    /**
     * The initial delay (in ticks) before the repeat delay should kick in
     */
    var initialDelay: Int = 10,
    /**
     * The repeat delay (in ticks) between each movement in the held direction
     */
    var repeatDelay: Int = 4,
    /**
     * Stick Hysteresis:
     *
     * The minimum analogue value required to begin scrolling in a direction.
     *
     * Prevents accidental input from controller drift, bounce back,
     * or small jitters.
     */
    var engageThreshold: Float = 0.25f,
    /**
     * Stick Hysteresis:
     *
     * The stick's analogue value must return below this threshold before
     * releasing the input.
     *
     * Prevents the controller spring from bouncing in the opposite direction,
     * counting as an input.
     */
    var releaseThreshold: Float = 0.10f,
    /**
     * Whether controller input should actively apply to THIS
     * [computer.obscure.piku.mod.fabric.ui.components.FlowNode].
     */
    var active: Boolean = false,

    /**
     * The list of actions that can scroll this [computer.obscure.piku.mod.fabric.ui.components.FlowNode].
     */
    var actions: List<ActionEvent> = listOf(
        ActionEvent.MOVE,
        ActionEvent.LOOK
    ),

    /**
     * The axis to take analogue input from in order to scroll.
     */
    var scrollAxis: ControllerScrollAxis = ControllerScrollAxis.HORIZONTAL,

    /**
     * Which [BindingEvent] should be used to determine when a
     * valid activation occurs.
     */
    var activateBindingEvent: BindingEvent = BindingEvent.TAP,

    /**
     * Which [ControllerBind]s should be used to control what
     * inputs on the controller determines a valid input.
     */
    var activateBindings: List<ControllerBind> = listOf(
        ControllerBind.JUMP
    ),
    /**
     * Determines how [activateBindings] translate into activation of
     * [computer.obscure.piku.mod.fabric.ui.components.UINode]s.
     */
    var activateMode: ControllerActivateMode = ControllerActivateMode.TOGGLE,

    /**
     * Determines how edges are handled.
     * [ControllerEdgeMode.WRAP]: holding the input will continuously wrap
     * around the selection once an edge is reached
     * [ControllerEdgeMode.REQUIRE_NUDGE]: holding the input will stop
     * once an edge is reached, and an additional input is required to wrap
     * back around.
     * [ControllerEdgeMode.STOP]: holding the input will stop entirely
     * once an edge is reached.
     */
    var edgeMode: ControllerEdgeMode = ControllerEdgeMode.REQUIRE_NUDGE
)

enum class ControllerActivateMode { MULTI, TOGGLE, SINGLE_TOGGLE, NONE }
enum class ControllerEdgeMode { WRAP, REQUIRE_NUDGE, STOP }

enum class ControllerScrollAxis {
    HORIZONTAL,
    VERTICAL
}