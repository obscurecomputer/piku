package computer.obscure.piku.fabric.ui.events

import computer.obscure.piku.common.classes.Vec2
import computer.obscure.piku.common.ui.components.Component
import computer.obscure.piku.common.ui.events.PropertyAnimation
import computer.obscure.piku.common.ui.events.SizeEvent

class SizeEventHandler : UIEventHandler<SizeEvent> {
    override fun handle(event: SizeEvent, component: Component, context: UIEventContext) {
        val anim = PropertyAnimation(
            targetId = event.targetId,
            getter = { c: Component -> c.props.size },
            setter = { c, value ->
                var x = value.x
                var y = value.y
                if (value.x == 0.0) x = c.props.size.x
                if (value.y == 0.0) y = c.props.size.y

                c.props.size = Vec2(x, y)
            },
            from = null,
            to = event.size,
            durationSeconds = event.durationSeconds,
            easing = event.easing
        )
        context.enqueueAnimation(anim)
    }
}