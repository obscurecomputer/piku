package me.znotchill.piku.common.ui.events

import me.znotchill.piku.common.ui.UIWindow

sealed class UIEvent {
    abstract val targetId: String
    abstract var delay: Long

    var window: UIWindow? = null
}