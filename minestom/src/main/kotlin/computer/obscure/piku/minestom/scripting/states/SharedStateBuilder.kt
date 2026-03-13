package computer.obscure.piku.minestom.scripting.states

import computer.obscure.piku.core.scripting.server.SharedStateManager
import computer.obscure.piku.core.states.SharedState
import net.minestom.server.entity.Player

fun Player.sharedState(
    name: String,
    block: SharedState.() -> Unit
): SharedState {
    val state = SharedState(
        name = name
    ).apply(block)

    // Send the initial state to the client
    val allOwners = mutableListOf<Player>()
    allOwners.add(this)

    when (val o = state.owners) {
        is Player -> allOwners.add(o)
        is Iterable<*> -> allOwners.addAll(o.filterIsInstance<Player>())
    }

    state.owners = allOwners
    SharedStateManager.addState(state)
    SharedStateManager.piku.syncStateToOwners(allOwners, state)

    return state
}

fun Player.getState(name: String): SharedState? {
    return SharedStateManager.getStateIfOwner(name, this)
}