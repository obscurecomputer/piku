package computer.obscure.piku.mod.common.scripting.api

import computer.obscure.piku.core.scripting.server.SharedStateManager
import computer.obscure.piku.core.states.SharedState
import computer.obscure.piku.mod.common.Piku
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative
import computer.obscure.twine.nativex.conversion.Converter.toLuaValue
import org.luaj.vm2.LuaValue
import java.util.UUID

class LuaSharedState(
    @TwineNativeProperty
    var internalId: String,
    @TwineNativeProperty
    var name: String,
    @TwineNativeProperty
    var value: LuaValue,
    @TwineNativeProperty
    var clientModifiable: Boolean,
) : TwineNative() {
    @TwineNativeProperty
    var onSet: LuaValue = LuaValue.NIL
        set(value) {
            field = value
            Piku.engine.events.stateCallbacks[UUID.fromString(this.internalId)] = value
        }

    @TwineNativeFunction
    fun set(value: Any) {
        if (!clientModifiable) {
            throw IllegalAccessException("SharedState '$name' is not client modifiable!")
        }
        val state = this.toSharedState()
        state.value = value

        Piku.engine.events.sendState(state)
        SharedStateManager.addState(state)
    }

    fun toSharedState(): SharedState {
        return SharedState(
            internalId = UUID.fromString(internalId),
            name = name,
            value = value,
            clientModifiable = clientModifiable
        )
    }

    companion object {
        fun fromSharedState(state: SharedState): LuaSharedState {
            return LuaSharedState(
                internalId = state.internalId.toString(),
                name = state.name,
                value = state.value.toLuaValue(),
                clientModifiable = state.clientModifiable
            )
        }
    }
}