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