package computer.obscure.piku.mod.fabric.animation

import computer.obscure.piku.core.classes.Easing

/**
 * The definition of an Animation.
 */
data class Animation<T>(
    /**
     * The id of the animation.
     * Usually derived from [computer.obscure.piku.mod.fabric.ui.components.UINode].
     * The parameter can be any string, not just a UINode ID.
     */
    val targetId: String = "",
    /**
     * The duration of the animation
     */
    val durationSeconds: Double,
    /**
     * The name of an easing, registered from [computer.obscure.piku.mod.fabric.ui.UIRenderer]
     */
    val easing: String = Easing.LINEAR.name,
    /**
     * The final value of this animation
     */
    val to: T,
    /**
     * The initial starting point of this animation
     */
    val getter: () -> T,
    /**
     * The setter, changing the getter's values to the animated ones
     */
    val setter: (T) -> Unit,

    val onStart: () -> Unit = {},
    val onFinish: () -> Unit = {},

    /**
     * Runs every tick of this animation.
     * (deltaSeconds, time)
     */
    val onTick: (Double, Double) -> Unit = { _, _ -> }
) {
    var from: T? = null
    var elapsed = 0.0
    var started = false
    var finished = false

    companion object {
        /**
         * Create an instant animation
         * idk why this exists
         */
        fun <T> instant(
            to: T,
            setter: (T) -> Unit,
            getter: () -> T,
        ): Animation<T> {
            return Animation(
                durationSeconds = 0.0,
                to = to,
                getter = getter,
                setter = setter
            )
        }
    }
}