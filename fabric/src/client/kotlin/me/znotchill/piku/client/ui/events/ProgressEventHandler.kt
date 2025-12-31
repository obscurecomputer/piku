package me.znotchill.piku.client.ui.events

import me.znotchill.piku.common.ui.components.Component
import me.znotchill.piku.common.ui.components.ProgressBar
import me.znotchill.piku.common.ui.events.ProgressEvent
import me.znotchill.piku.common.ui.events.PropertyAnimation

class ProgressEventHandler : UIEventHandler<ProgressEvent> {
    override fun handle(event: ProgressEvent, component: Component, context: UIEventContext) {
        val anim = PropertyAnimation(
            targetId = event.targetId,
            getter = { c: ProgressBar -> c.props.progress },
            setter = { c: ProgressBar, value -> c.props.progress = value },
            from = null,
            to = event.progress,
            durationSeconds = event.durationSeconds,
            easing = event.easing.toString()
        )
        anim.window = event.window
        context.enqueueAnimation(anim)
    }
}