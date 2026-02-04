package computer.obscure.piku.mod.common.ui.events

import computer.obscure.piku.core.classes.Vec2
import computer.obscure.piku.core.ui.components.Component
import computer.obscure.piku.core.ui.events.MoveEvent
import computer.obscure.piku.core.ui.events.PropertyAnimation
import computer.obscure.piku.mod.common.ui.UIRenderer

class MoveEventHandler : UIEventHandler<MoveEvent> {
    override fun handle(event: MoveEvent, component: Component, context: UIEventContext) {
        val anim = PropertyAnimation(
            targetId = event.targetId,
            getter = { c: Component -> c.props.pos },
            setter = { c, value ->
                var x = value.x
                var y = value.y
                if (value.x == 0.0) x = c.props.pos.x
                if (value.y == 0.0) y = c.props.pos.y

                UIRenderer.markDependentsDirty(c.internalId)
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