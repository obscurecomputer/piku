package computer.obscure.piku.core.ui.events

import computer.obscure.piku.core.ui.classes.Spacing

data class PaddingEvent(
    override var delay: Long,
    override val targetId: String,
    val padding: Spacing,
    val durationSeconds: Double,
    val easing: String,
) : UIEvent()