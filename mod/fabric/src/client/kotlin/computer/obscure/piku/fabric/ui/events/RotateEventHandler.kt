package computer.obscure.piku.fabric.ui.events

import computer.obscure.piku.common.ui.components.Component
import computer.obscure.piku.common.ui.events.PropertyAnimation
import computer.obscure.piku.common.ui.events.RotateEvent

class RotateEventHandler : UIEventHandler<RotateEvent> {
    override fun handle(event: RotateEvent, component: Component, context: UIEventContext) {
        val anim = PropertyAnimation(
            targetId = event.targetId,
            getter = { c: Component -> c.props.rotation },
            setter = { c, value -> c.props.rotation = value },
            from = null,
            to = event.rotation,
            durationSeconds = event.durationSeconds,
            easing = event.easing
        )
        context.enqueueAnimation(anim)
    }
}