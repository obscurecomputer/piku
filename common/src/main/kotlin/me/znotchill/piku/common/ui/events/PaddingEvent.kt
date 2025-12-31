package me.znotchill.piku.common.ui.events

import me.znotchill.piku.common.ui.classes.Easing
import me.znotchill.piku.common.ui.classes.Spacing

data class PaddingEvent(
    override var delay: Long,
    override val targetId: String,
    val padding: Spacing,
    val durationSeconds: Double,
    val easing: Easing,
) : UIEvent()