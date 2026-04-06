package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative

class LuaScheduler : TwineNative("scheduler") {

    @TwineFunction
    fun task(fn: (LuaTask) -> Unit): LuaTaskBuilder {
        return LuaTaskBuilder(fn)
    }
}