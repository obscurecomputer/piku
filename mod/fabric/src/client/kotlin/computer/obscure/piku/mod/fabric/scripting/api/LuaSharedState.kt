package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.piku.core.scripting.server.SharedStateManager
import computer.obscure.piku.core.states.SharedState
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import java.util.*

class LuaSharedState(
    @TwineProperty var internalId: String,
    @TwineProperty var name: String,
    @TwineProperty var value: Any?,
    @TwineProperty var clientModifiable: Boolean,
) : TwineNative("sharedState") {

    var onSet: ((Map<String, Any?>) -> Unit)? = null
        set(value) {
            field = value
            if (value != null) {
                PikuClient.engine.events.stateCallbacks[UUID.fromString(internalId)] = value
            }
        }

    @TwineFunction
    fun set(value: Any) {
        if (!clientModifiable) throw IllegalAccessException("SharedState '$name' is not client modifiable!")
        val state = toSharedState()
        state.value = value
        PikuClient.engine.events.sendState(state)
        SharedStateManager.addState(state)
    }

    fun toSharedState() = SharedState(
        internalId = UUID.fromString(internalId),
        name = name,
        value = value,
        clientModifiable = clientModifiable
    )

    companion object {
        fun fromSharedState(state: SharedState) = LuaSharedState(
            internalId = state.internalId.toString(),
            name = state.name,
            value = state.value,
            clientModifiable = state.clientModifiable
        )
    }
}