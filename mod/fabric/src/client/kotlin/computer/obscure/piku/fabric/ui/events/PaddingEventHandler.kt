package computer.obscure.piku.fabric.ui.events

import computer.obscure.piku.common.ui.components.Component
import computer.obscure.piku.common.ui.events.PaddingEvent
import computer.obscure.piku.common.ui.events.PropertyAnimation

class PaddingEventHandler : UIEventHandler<PaddingEvent> {
    override fun handle(event: PaddingEvent, component: Component, context: UIEventContext) {
        val anim = PropertyAnimation(
            targetId = event.targetId,
            getter = { c: Component -> c.props.padding },
            setter = { c, value -> c.props.padding = value },
            from = null,
            to = event.padding,
            durationSeconds = event.durationSeconds,
            easing = event.easing
        )
        context.enqueueAnimation(anim)
    }
}