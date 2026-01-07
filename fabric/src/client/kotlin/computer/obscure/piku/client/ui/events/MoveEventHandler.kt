package computer.obscure.piku.client.ui.events

import computer.obscure.piku.common.classes.Vec2
import computer.obscure.piku.common.ui.components.Component
import computer.obscure.piku.common.ui.events.MoveEvent
import computer.obscure.piku.common.ui.events.PropertyAnimation

class MoveEventHandler : UIEventHandler<MoveEvent> {
    override fun handle(event: MoveEvent, component: Component, context: UIEventContext) {
        val anim = PropertyAnimation(
            targetId = event.targetId,
            getter = { c: Component -> c.props.pos },
            setter = { c, value ->
                var x = value.x
                var y = value.y
                if (value.x == 0f) x = c.props.pos.x
                if (value.y == 0f) y = c.props.pos.y

                c.props.pos = Vec2(x, y)
            },
            from = null,
            to = event.position,
            durationSeconds = event.durationSeconds,
            easing = event.easing
        )
        context.enqueueAnimation(anim)
    }
}