package computer.obscure.piku.core.scripting.server

import computer.obscure.piku.core.states.SharedState
import java.util.UUID

object SharedStateManager {
    private val sharedStates = mutableMapOf<UUID, SharedState>()
    lateinit var piku: ServerAPI<*>

    fun getState(internalId: UUID): SharedState? =
        sharedStates[internalId]

    fun addState(state: SharedState) {
        if (sharedStates[state.internalId] != null) return
        sharedStates[state.internalId] = state
    }
}