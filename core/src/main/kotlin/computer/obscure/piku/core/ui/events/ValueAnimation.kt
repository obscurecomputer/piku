package computer.obscure.piku.core.ui.events

class ValueAnimation<T>(
    val durationSeconds: Double,
    val easing: String,
    val getter: () -> T,
    val setter: (T) -> Unit,
    val to: T
) {
    var elapsed = 0.0
    var from: T? = null
}