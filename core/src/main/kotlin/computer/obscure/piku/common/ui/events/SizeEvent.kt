package computer.obscure.piku.common.ui.events

import computer.obscure.piku.common.classes.Vec2

data class SizeEvent(
    override var delay: Long,
    override val targetId: String,
    val size: Vec2,
    val durationSeconds: Double,
    val easing: String,
) : UIEvent()