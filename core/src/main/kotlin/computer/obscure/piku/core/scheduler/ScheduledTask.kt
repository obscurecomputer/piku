package computer.obscure.piku.core.scheduler

class ScheduledTask(
    val action: (String) -> Unit
) {
    lateinit var id: String
    var delayTicks: Long = 0
    var repeatTicks: Long? = null
    var cancelled = false

    internal var nextRunTick: Long = 0

    fun delay(ticks: Long) {
        delayTicks = ticks
    }

    fun repeat(ticks: Long) {
        repeatTicks = ticks
    }

    fun cancel() {
        cancelled = true
    }

    internal fun start(currentTick: Long) {
        nextRunTick = currentTick + delayTicks
    }

    internal fun run() {
        action(id)
    }
}
