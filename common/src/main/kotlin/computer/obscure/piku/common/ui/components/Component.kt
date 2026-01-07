package computer.obscure.piku.common.ui.components

import computer.obscure.piku.common.classes.Vec2
import computer.obscure.piku.common.ui.UIEventQueue
import computer.obscure.piku.common.ui.UIWindow
import computer.obscure.piku.common.ui.classes.CompType
import computer.obscure.piku.common.ui.classes.Easing
import computer.obscure.piku.common.ui.classes.RelativePosition
import computer.obscure.piku.common.ui.classes.Spacing
import computer.obscure.piku.common.ui.components.props.BaseProps
import computer.obscure.piku.common.ui.events.MoveEvent
import computer.obscure.piku.common.ui.events.OpacityEvent
import computer.obscure.piku.common.ui.events.PaddingEvent
import computer.obscure.piku.common.ui.events.RotateEvent
import java.util.UUID

sealed class Component {
    var name: String = ""
    val internalId: String = UUID.randomUUID().toString()
    var relativeTo: String? = null
    var relativePosition: RelativePosition? = null
    abstract val compType: CompType
    abstract val props: BaseProps

    abstract fun width(): Float
    abstract fun height(): Float

    var screenX: Int = 0
    var screenY: Int = 0
    var computedScale: Vec2 = Vec2(0f, 0f)
    var computedSize: Vec2? = null
    var computedPos: Vec2? = null
}

fun Component.move(
    to: Vec2,
    duration: Double = 0.0,
    easing: String,
    delay: Long = 0L
): MoveEvent {
    val event = MoveEvent(
        targetId = this.internalId,
        delay = delay,
        position = to,
        durationSeconds = duration,
        easing = easing
    )

    UIEventQueue.enqueueNow(event)
    return event
}

fun Component.rotate(
    rotation: Int,
    duration: Double = 0.0,
    easing: Easing = Easing.LINEAR,
    delay: Long = 0L
): RotateEvent {
    val event = RotateEvent(
        targetId = this.internalId,
        delay = delay,
        rotation = rotation,
        durationSeconds = duration,
        easing = easing
    )

    UIEventQueue.enqueueNow(event)
    return event
}


fun Component.opacity(
    /**
     * Opacity between 0-1
     */
    opacity: Float,
    duration: Double = 0.0,
    easing: Easing = Easing.LINEAR,
    delay: Long = 0L
): OpacityEvent {
    val event = OpacityEvent(
        targetId = this.internalId,
        delay = delay,
        opacity = opacity,
        durationSeconds = duration,
        easing = easing
    )

    UIEventQueue.enqueueNow(event)
    return event
}

fun Component.padding(
    padding: Spacing,
    duration: Double = 0.0,
    easing: Easing = Easing.LINEAR,
    delay: Long = 0L
): PaddingEvent {
    val event = PaddingEvent(
        targetId = this.internalId,
        delay = delay,
        padding = padding,
        durationSeconds = duration,
        easing = easing
    )

    UIEventQueue.enqueueNow(event)
    return event
}

fun <T : Component> T.schedule(delay: Long, block: T.() -> Unit) {
    UIEventQueue.enqueueDelayed(delay) {
        this.block()
    }
}

infix fun <T : Component> T.relative(component: Component): T {
    this.relativeTo = component.internalId
    return this
}

infix fun <T : Component> T.rightOf(component: Component): T {
    this.relativeTo = component.internalId
    this.relativePosition = RelativePosition.RIGHT_OF
    return this
}

infix fun <T : Component> T.leftOf(component: Component): T {
    this.relativeTo = component.internalId
    this.relativePosition = RelativePosition.LEFT_OF
    return this
}

infix fun <T : Component> T.topOf(component: Component): T {
    this.relativeTo = component.internalId
    this.relativePosition = RelativePosition.ABOVE
    return this
}

infix fun <T : Component> T.bottomOf(component: Component): T {
    this.relativeTo = component.internalId
    this.relativePosition = RelativePosition.BELOW
    return this
}
