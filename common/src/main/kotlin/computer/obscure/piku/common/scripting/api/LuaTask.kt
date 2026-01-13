package computer.obscure.piku.common.scripting.api

import computer.obscure.piku.common.scheduler.Scheduler
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative

class LuaTask : TwineNative() {

    private var taskId: String? = null

    fun bind(id: String) {
        taskId = id
    }

    @TwineNativeFunction
    fun cancel() {
        val id = taskId ?: return
        Scheduler.get(id)?.cancel()
    }
}