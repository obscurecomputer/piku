package computer.obscure.piku.mod.fabric.scripting

import computer.obscure.piku.core.scripting.server.SharedStateManager
import computer.obscure.piku.mod.fabric.scripting.api.LuaSharedState
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction

class LuaStateManager : TwineNative("state") {
    @TwineFunction("get")
    fun getState(name: String): LuaSharedState? {
        val state = SharedStateManager.getState(name) ?: return null

        return LuaSharedState.fromSharedState(state)
    }
}