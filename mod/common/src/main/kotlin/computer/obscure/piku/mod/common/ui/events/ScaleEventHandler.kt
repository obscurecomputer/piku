package computer.obscure.piku.mod.common.ui.events

import computer.obscure.piku.core.classes.Vec2
import computer.obscure.piku.core.ui.components.Component
import computer.obscure.piku.core.ui.events.PropertyAnimation
import computer.obscure.piku.core.ui.events.ScaleEvent

class ScaleEventHandler : UIEventHandler<ScaleEvent> {
    override fun handle(event: ScaleEvent, component: Component, context: UIEventContext) {
        val anim = PropertyAnimation(
            targetId = event.targetId,
            getter = { c: Component -> c.props.scale },
            setter = { c, value ->
                var x = value.x
                var y = value.y
                if (value.x == 0.0) x = c.props.scale.x
                if (value.y == 0.0) y = c.props.scale.y

                c.props.scale = Vec2(x, y)
            },
            from = null,
            to = event.scale,
            durationSeconds = event.durationSeconds,
            easing = event.easing
        )
        context.enqueueAnimation(anim)
    }
}