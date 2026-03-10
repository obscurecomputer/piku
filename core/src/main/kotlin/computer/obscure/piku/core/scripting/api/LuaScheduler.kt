package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative

class LuaScheduler : TwineNative("scheduler") {

    @TwineNativeFunction
    fun task(fn: (LuaTask) -> Unit): LuaTaskBuilder {
        return LuaTaskBuilder(fn)
    }
}