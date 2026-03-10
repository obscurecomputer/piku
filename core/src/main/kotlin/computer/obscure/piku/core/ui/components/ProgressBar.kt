package computer.obscure.piku.core.ui.components

import computer.obscure.piku.core.ui.UIEventQueue
import computer.obscure.piku.core.ui.classes.CompType
import computer.obscure.piku.core.ui.components.props.ProgressBarProps
import computer.obscure.piku.core.ui.events.ProgressEvent

open class ProgressBar(
    override val props: ProgressBarProps
) : Component() {
    override val compType: CompType = CompType.PROGRESS_BAR

    override fun width(): Double {
        return props.size.x
    }

    override fun height(): Double {
        return props.size.y
    }
}

fun ProgressBar.progress(
    progress: Float,
    duration: Double = 0.0,
    easing: String,
    delay: Long = 0L
): ProgressEvent {
    val event = ProgressEvent(
        targetId = this.internalId,
        delay = delay,
        progress = progress,
        durationSeconds = duration,
        easing = easing
    )

    UIEventQueue.enqueueNow(event)
    return event
}