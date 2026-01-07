package computer.obscure.piku.common.ui.components

import computer.obscure.piku.common.ui.UIEventQueue
import computer.obscure.piku.common.ui.classes.CompType
import computer.obscure.piku.common.ui.classes.Easing
import computer.obscure.piku.common.ui.components.props.ProgressBarProps
import computer.obscure.piku.common.ui.events.ProgressEvent

open class ProgressBar(
    override val props: ProgressBarProps
) : Component() {
    override val compType: CompType = CompType.PROGRESS_BAR

    override fun width(): Float {
        return props.size.x
    }

    override fun height(): Float {
        return props.size.y
    }
}

fun ProgressBar.progress(
    progress: Float,
    duration: Double = 0.0,
    easing: Easing = Easing.LINEAR,
    delay: Long = 0L
): ProgressEvent {
    val event = ProgressEvent(
        targetId = this.name,
        delay = delay,
        progress = progress,
        durationSeconds = duration,
        easing = easing
    )

    UIEventQueue.enqueueNow(event)
    return event
}