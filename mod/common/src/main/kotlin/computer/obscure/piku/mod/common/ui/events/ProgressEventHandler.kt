package computer.obscure.piku.mod.common.ui.events

import computer.obscure.piku.core.ui.components.Component
import computer.obscure.piku.core.ui.components.ProgressBar
import computer.obscure.piku.core.ui.events.ProgressEvent
import computer.obscure.piku.core.ui.events.PropertyAnimation

class ProgressEventHandler : UIEventHandler<ProgressEvent> {
    override fun handle(event: ProgressEvent, component: Component, context: UIEventContext) {
        val anim = PropertyAnimation(
            targetId = event.targetId,
            getter = { c: ProgressBar -> c.props.progress },
            setter = { c: ProgressBar, value -> c.props.progress = value },
            from = null,
            to = event.progress,
            durationSeconds = event.durationSeconds,
            easing = event.easing
        )
        context.enqueueAnimation(anim)
    }
}