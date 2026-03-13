package computer.obscure.piku.mod.common.scripting

import computer.obscure.piku.core.scripting.server.SharedStateManager
import computer.obscure.piku.mod.common.scripting.api.LuaSharedState
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative

class LuaStateManager : TwineNative("state") {
    @TwineNativeFunction("get")
    fun getState(name: String): LuaSharedState? {
        val state = SharedStateManager.getState(name) ?: return null

        return LuaSharedState.fromSharedState(state)
    }
}