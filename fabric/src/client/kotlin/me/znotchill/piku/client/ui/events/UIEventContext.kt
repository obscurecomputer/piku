package me.znotchill.piku.client.ui.events

import me.znotchill.piku.common.ui.UIWindow
import me.znotchill.piku.common.ui.events.PropertyAnimation

class UIEventContext(
    val currentWindow: () -> UIWindow?,
    val enqueueAnimation: (PropertyAnimation<*, *>) -> Unit
)