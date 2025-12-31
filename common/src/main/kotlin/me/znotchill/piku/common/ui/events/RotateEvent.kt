package me.znotchill.piku.common.ui.events

import me.znotchill.piku.common.ui.classes.Easing

data class RotateEvent(
    override var delay: Long,
    override val targetId: String,
    val rotation: Int,
    val durationSeconds: Double,
    val easing: Easing,
) : UIEvent() {
    var elapsed = 0.0
    var start: Float? = null
}