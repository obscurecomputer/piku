package me.znotchill.piku.client.ui.events

import me.znotchill.piku.common.ui.components.Component
import me.znotchill.piku.common.ui.events.DestroyEvent

class DestroyEventHandler : UIEventHandler<DestroyEvent> {
    override fun handle(event: DestroyEvent, component: Component, context: UIEventContext) {
        context.currentWindow()?.components?.remove(event.targetId)
    }
}