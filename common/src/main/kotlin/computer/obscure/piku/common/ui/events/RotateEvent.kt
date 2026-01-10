package computer.obscure.piku.common.ui.events

import computer.obscure.piku.common.ui.classes.Easing

data class RotateEvent(
    override var delay: Long,
    override val targetId: String,
    val rotation: Int,
    val durationSeconds: Double,
    val easing: String,
) : UIEvent()