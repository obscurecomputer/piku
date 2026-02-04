package computer.obscure.piku.core.scripting.api

import computer.obscure.piku.core.scheduler.ScheduledTask
import computer.obscure.piku.core.scheduler.Scheduler
import computer.obscure.piku.core.scripting.engine.EngineError
import computer.obscure.piku.core.scripting.engine.EngineErrorCode
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative

class LuaTaskBuilder(
    private val fn: (LuaTask) -> Unit
) : TwineNative() {
    private val task = ScheduledTask { id ->
        val luaTask = LuaTask()
        luaTask.bind(id)

        try {
            fn(luaTask)
        } catch (e: Exception) {
            val error = EngineError(
                EngineErrorCode.GENERIC_ERROR,
                e.message ?: "Unknown"
            )
            error.printStackTrace() // do NOT crash the game
        }
    }

    @TwineNativeFunction
    fun delay(ticks: Long): LuaTaskBuilder {
        task.delay(ticks)
        return this
    }

    @TwineNativeFunction
    // repeat appears to be a reserved keyword in lua??
    fun loop(ticks: Int): LuaTaskBuilder {
        task.repeat(ticks.toLong())
        return this
    }

    @TwineNativeFunction
    fun run() {
        Scheduler.submit(task)
    }
}
