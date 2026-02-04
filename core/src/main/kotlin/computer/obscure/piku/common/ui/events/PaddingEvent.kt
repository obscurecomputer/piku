package computer.obscure.piku.common.ui.events

import computer.obscure.piku.common.ui.classes.Easing
import computer.obscure.piku.common.ui.classes.Spacing

data class PaddingEvent(
    override var delay: Long,
    override val targetId: String,
    val padding: Spacing,
    val durationSeconds: Double,
    val easing: String,
) : UIEvent()