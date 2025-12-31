package me.znotchill.piku.client.ui.events

import me.znotchill.piku.common.ui.components.Component
import me.znotchill.piku.common.ui.events.PropertyAnimation
import me.znotchill.piku.common.ui.events.RotateEvent

class RotateEventHandler : UIEventHandler<RotateEvent> {
    override fun handle(event: RotateEvent, component: Component, context: UIEventContext) {
        val anim = PropertyAnimation(
            targetId = event.targetId,
            getter = { c: Component -> c.props.rotation },
            setter = { c, value -> c.props.rotation = value },
            from = null,
            to = event.rotation,
            durationSeconds = event.durationSeconds,
            easing = event.easing.toString()
        )
        anim.window = event.window
        context.enqueueAnimation(anim)
    }
}