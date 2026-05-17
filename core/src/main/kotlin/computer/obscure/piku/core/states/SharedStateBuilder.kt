package computer.obscure.piku.core.states

import computer.obscure.piku.core.scripting.server.SharedStateManager

fun sharedState(
    name: String,
    block: SharedState.() -> Unit
): SharedState {
    val state = SharedState(
        name = name
    ).apply(block)

    SharedStateManager.addState(state)

    // Send the initial state to the client
    SharedStateManager.piku.syncStateToOwners(
        state.owners,
        state
    )

    return state
}

fun StateOwner.sharedState(
    name: String,
    block: SharedState.() -> Unit
): SharedState {

    val state = SharedState(name).apply(block)

    val owners = mutableListOf<StateOwner>()
    owners.add(this)

    when (val extraOwners = state.owners) {
        is StateOwner -> owners.add(extraOwners)
        is Iterable<*> -> owners.addAll(extraOwners.filterIsInstance<StateOwner>())
    }

    state.owners = owners

    SharedStateManager.addState(state)
    SharedStateManager.piku.syncStateToOwners(owners, state)

    return state
}

fun <T : StateOwner> T.getState(name: String): SharedState? {
    return SharedStateManager.getStateIfOwner(name, this as Any)
}