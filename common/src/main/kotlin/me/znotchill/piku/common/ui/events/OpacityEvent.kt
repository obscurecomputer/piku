package me.znotchill.piku.common.ui.events

import me.znotchill.piku.common.ui.classes.Easing

data class OpacityEvent(
    override var delay: Long,
    override val targetId: String,
    val opacity: Float,
    val durationSeconds: Double,
    val easing: Easing,
) : UIEvent() {
    var elapsed = 0.0
    var start: Float? = null
}