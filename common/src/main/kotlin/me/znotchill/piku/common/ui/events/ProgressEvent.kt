package me.znotchill.piku.common.ui.events

import me.znotchill.piku.common.ui.classes.Easing

data class ProgressEvent(
    override var delay: Long,
    override val targetId: String,
    val progress: Float,
    val durationSeconds: Double,
    val easing: Easing,
) : UIEvent()