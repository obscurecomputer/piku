package computer.obscure.piku.client.ui.events

import computer.obscure.piku.common.classes.Vec2
import computer.obscure.piku.common.ui.components.Component
import computer.obscure.piku.common.ui.components.Line
import computer.obscure.piku.common.ui.events.LineToEvent
import computer.obscure.piku.common.ui.events.PropertyAnimation

class LineToEventHandler : UIEventHandler<LineToEvent> {
    override fun handle(event: LineToEvent, component: Component, context: UIEventContext) {
        val anim = PropertyAnimation(
            targetId = event.targetId,
            getter = { c: Line -> c.props.to },
            setter = { c, value ->
                var x = value.x
                var y = value.y
                if (value.x == 0.0) x = c.props.to.x
                if (value.y == 0.0) y = c.props.to.y

                c.props.to = Vec2(x, y)
            },
            from = null,
            to = event.position,
            durationSeconds = event.durationSeconds,
            easing = event.easing
        )
        context.enqueueAnimation(anim)
    }
}