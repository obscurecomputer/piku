package computer.obscure.piku.core.scripting.server

import computer.obscure.piku.core.service.PikuService
import computer.obscure.piku.core.states.SharedState
import java.util.UUID

object SharedStateManager : PikuService {
    private val sharedStates = mutableMapOf<UUID, SharedState>()
    lateinit var piku: ServerAPI<*>

    fun getState(internalId: UUID): SharedState? =
        sharedStates[internalId]

    fun getState(name: String): SharedState? =
        sharedStates.toList().firstOrNull { it.second.name == name }?.second

    fun addState(state: SharedState) {
        sharedStates[state.internalId] = state
    }

    override fun shutdown() {
        sharedStates.clear()
    }
}