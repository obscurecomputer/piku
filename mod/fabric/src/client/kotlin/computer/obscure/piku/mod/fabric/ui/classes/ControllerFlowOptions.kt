package computer.obscure.piku.mod.fabric.ui.classes

import computer.obscure.piku.mod.fabric.controlify.ActionEvent
import computer.obscure.piku.mod.fabric.controlify.BindingEvent
import dev.isxander.controlify.api.bind.InputBindingSupplier
import dev.isxander.controlify.bindings.ControlifyBindings

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
     * Which [InputBindingSupplier]s should be used to control what
     * inputs on the controller determines a valid input.
     */
    var activateBindings: List<InputBindingSupplier> = listOf(
        ControlifyBindings.JUMP
    ),
    /**
     * Determines how [activateBindings] translate into activation of
     * [computer.obscure.piku.mod.fabric.ui.components.UINode]s.
     */
    var activateMode: ControllerActivateMode = ControllerActivateMode.TOGGLE,
)

enum class ControllerActivateMode { MULTI, TOGGLE, SINGLE_TOGGLE, NONE }

enum class ControllerScrollAxis {
    HORIZONTAL,
    VERTICAL
}