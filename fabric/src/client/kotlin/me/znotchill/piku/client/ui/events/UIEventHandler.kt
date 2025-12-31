package me.znotchill.piku.client.ui.events

import me.znotchill.piku.common.ui.components.Component
import me.znotchill.piku.common.ui.events.UIEvent

interface UIEventHandler<E : UIEvent> {
    fun handle(event: E, component: Component, context: UIEventContext)
}