package computer.obscure.piku.mod.common.ui.events

import computer.obscure.piku.core.ui.components.Component
import computer.obscure.piku.core.ui.events.PropertyAnimation
import computer.obscure.piku.core.ui.events.RotateEvent

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