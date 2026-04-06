package computer.obscure.piku.core.scripting.api

import computer.obscure.piku.core.scheduler.Scheduler
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative

class LuaTask : TwineNative() {

    private var taskId: String? = null

    fun bind(id: String) {
        taskId = id
    }

    @TwineFunction
    fun cancel() {
        val id = taskId ?: return
        Scheduler.get(id)?.cancel()
    }
}