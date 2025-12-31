package me.znotchill.piku.common.ui.events

data class DestroyEvent(
    override var delay: Long,
    override val targetId: String
) : UIEvent()