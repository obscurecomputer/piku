package computer.obscure.piku.common.ui.events

import computer.obscure.piku.common.classes.Vec2
import computer.obscure.piku.common.ui.classes.Easing

data class MoveEvent(
    override var delay: Long,
    override val targetId: String,
    val position: Vec2,
    val durationSeconds: Double,
    val easing: Easing,
) : UIEvent() {
    var elapsed = 0.0
    var start: Vec2? = null
}