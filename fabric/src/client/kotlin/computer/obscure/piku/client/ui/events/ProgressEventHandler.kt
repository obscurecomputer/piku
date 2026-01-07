package computer.obscure.piku.client.ui.events

import computer.obscure.piku.common.ui.components.Component
import computer.obscure.piku.common.ui.components.ProgressBar
import computer.obscure.piku.common.ui.events.ProgressEvent
import computer.obscure.piku.common.ui.events.PropertyAnimation

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
        context.enqueueAnimation(anim)
    }
}