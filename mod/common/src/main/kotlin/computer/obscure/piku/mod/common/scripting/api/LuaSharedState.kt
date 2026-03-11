package computer.obscure.piku.mod.common.scripting.api

import computer.obscure.piku.core.states.SharedState
import computer.obscure.piku.mod.common.Piku
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative
import computer.obscure.twine.nativex.conversion.Converter.toKotlinType
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
    @TwineNativeFunction
    fun set(value: Any) {
        if (!clientModifiable) {
            throw IllegalAccessException("SharedState '$name' is not client modifiable!")
        }
        val state = this.toSharedState()
        state.value = value
        Piku.engine.events.sendState(state)
    }

    fun toSharedState(): SharedState {
        return SharedState(
            internalId = UUID.fromString(internalId),
            name = name,
            value = value,
            clientModifiable = clientModifiable
        )
    }
}