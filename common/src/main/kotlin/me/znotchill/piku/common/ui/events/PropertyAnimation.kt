package me.znotchill.piku.common.ui.events

import me.znotchill.piku.common.ui.components.Component

data class PropertyAnimation<C : Component, T>(
    override val targetId: String,
    override var delay: Long = 0L,
    val getter: (C) -> T,
    val setter: (C, T) -> Unit,
    var from: T? = null,
    val to: T,
    val durationSeconds: Double,
    val easing: String = "linear"
) : UIEvent() {
    var elapsed = 0.0
}