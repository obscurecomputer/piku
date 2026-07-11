package computer.obscure.piku.mod.fabric.ui.classes

import computer.obscure.piku.mod.fabric.ui.components.UINode

/**
 * All the data handled internally by Piku,
 * separate from the [ControllerFlowOptions].
 */
data class ControllerFlowData(
    var currentSelectionIndex: Int = 0,
    var currentSelection: UINode? = null,
    var previousSelectionIndex: Int = 0,
    var previousSelection: UINode? = null,
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
    /**
     * Whether this node is focused, and should take input.
     */
    var focused: Boolean = true,
    /**
     * Whether the scroller has reached the edge of the node selection.
     */
    var atEdgeDirection: ControllerScrollDirection = ControllerScrollDirection.NO_INPUT,
    /**
     * Whether a new input was just engaged and not yet executed.
     */
    var justEngaged: Boolean = false,
    /**
     * Whether the scroller reached the edge, and has received a new input to nudge wrap.
     * Only applies when [ControllerFlowOptions.edgeMode] is [ControllerEdgeMode.REQUIRE_NUDGE]
     */
    var nudged: Boolean = false
)

enum class ControllerScrollDirection(val direction: Int) {
    BACKWARD(-1),
    NO_INPUT(0),
    FORWARD(1)
}