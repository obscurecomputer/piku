package computer.obscure.piku.mod.common.ui.events

import computer.obscure.piku.core.classes.Vec2
import computer.obscure.piku.core.ui.components.Component
import computer.obscure.piku.core.ui.components.Line
import computer.obscure.piku.core.ui.events.LineToEvent
import computer.obscure.piku.core.ui.events.PropertyAnimation

class LineFromEventHandler : UIEventHandler<LineToEvent> {
    override fun handle(event: LineToEvent, component: Component, context: UIEventContext) {
        val anim = PropertyAnimation(
            targetId = event.targetId,
            getter = { c: Line -> c.props.from },
            setter = { c, value ->
                var x = value.x
                var y = value.y
                if (value.x == 0.0) x = c.props.from.x
                if (value.y == 0.0) y = c.props.from.y

                c.props.from = Vec2(x, y)
            },
            from = null,
            to = event.position,
            durationSeconds = event.durationSeconds,
            easing = event.easing
        )
        context.enqueueAnimation(anim)
    }
}