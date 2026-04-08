package computer.obscure.piku.mod.fabric.animation

data class Animation<T>(
    val durationSeconds: Double,
    val easing: String,
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
}