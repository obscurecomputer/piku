package computer.obscure.piku.mod.fabric.ui.classes

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
)