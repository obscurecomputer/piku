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

    return state
}