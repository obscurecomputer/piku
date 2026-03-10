package computer.obscure.piku.core.ui.events

import computer.obscure.piku.core.classes.Vec2

data class SizeEvent(
    override var delay: Long,
    override val targetId: String,
    val size: Vec2,
    val durationSeconds: Double,
    val easing: String,
) : UIEvent()