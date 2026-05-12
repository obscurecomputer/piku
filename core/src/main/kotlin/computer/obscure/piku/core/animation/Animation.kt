package computer.obscure.piku.core.animation

import computer.obscure.piku.core.ui.classes.Easing

data class Animation<T>(
    val targetId: String = "",
    val durationSeconds: Double,
    val easing: String = Easing.LINEAR.name,
    val to: T,
    val getter: () -> T,
    val setter: (T) -> Unit,
    val onStart: () -> Unit = {},
    val onFinish: () -> Unit = {},

    // deltaSeconds, t
    val onTick: (Double, Double) -> Unit = { _, _ -> }
) {
    var from: T? = null
    var elapsed = 0.0
    var started = false
    var finished = false

    companion object {
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