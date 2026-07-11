package computer.obscure.piku.mod.fabric.ui.classes

import computer.obscure.piku.mod.fabric.ui.components.UINode

/**
 * All the data handled internally by Piku,
 * separate from the [ControllerFlowOptions].
 */
data class ControllerFlowData(
    var currentSelectionIndex: Int = 0,
    var currentSelection: UINode? = null,
    /**
     * Whether the controller has given enough analogue input within
     * the [ControllerFlowOptions.initialDelay]
     */
    var holding: Boolean = false,
    var ticksScrolled: Int = 0,
    /**
     * Current movement direction directly from the controller's analogue value
     */
    var direction: ControllerScrollDirection = ControllerScrollDirection.NO_INPUT,
    /**
     * The *LOCKED* movement direction used for scrolling
     */
    var heldDirection: ControllerScrollDirection = ControllerScrollDirection.NO_INPUT,
    /**
     * Whether the input has reached the repeating phase.
     *
     * Will be false when waiting for the [ControllerFlowOptions.initialDelay]
     * to be reached.
     *
     * When true, selection will continue moving at the
     * [ControllerFlowOptions.repeatDelay].
     */
    var repeating: Boolean = false,
)

enum class ControllerScrollDirection(val direction: Int) {
    LEFT(-1),
    NO_INPUT(0),
    RIGHT(1)
}