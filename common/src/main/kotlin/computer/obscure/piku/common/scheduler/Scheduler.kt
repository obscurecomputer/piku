package computer.obscure.piku.common.scheduler

import java.util.UUID


object Scheduler {

    private val tasks = LinkedHashMap<String, ScheduledTask>()
    private val pendingAdd = mutableListOf<Pair<String, ScheduledTask>>()
    private val pendingRemove = mutableListOf<String>()

    private var tick = 0L
    private var ticking = false

    fun reset() {
        tasks.clear()
        pendingAdd.clear()
        pendingRemove.clear()
        tick = 0
    }

    fun tick() {
        tick++
        ticking = true

        val it = tasks.iterator()
        while (it.hasNext()) {
            val (id, task) = it.next()

            if (task.cancelled) {
                pendingRemove += id
                continue
            }

            if (tick >= task.nextRunTick) {
                task.run()

                if (task.repeatTicks != null) {
                    task.nextRunTick = tick + task.repeatTicks!!
                } else {
                    pendingRemove += id
                }
            }
        }

        ticking = false

        // remove after iteration to prevent CME
        for (id in pendingRemove) {
            tasks.remove(id)
        }
        pendingRemove.clear()

        for ((id, task) in pendingAdd) {
            tasks[id] = task
        }
        pendingAdd.clear()
    }

    fun submit(task: ScheduledTask): String {
        val id = UUID.randomUUID().toString()
        task.id = id
        task.start(tick)

        if (ticking) {
            pendingAdd += id to task
        } else {
            tasks[id] = task
        }

        return id
    }

    fun get(id: String): ScheduledTask? = tasks[id]
}