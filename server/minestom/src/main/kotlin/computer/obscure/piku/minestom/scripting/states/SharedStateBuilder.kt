package computer.obscure.piku.minestom.scripting.states

import computer.obscure.piku.core.states.SharedState
import computer.obscure.piku.core.states.getState
import computer.obscure.piku.core.states.sharedState
import net.minestom.server.entity.Player

fun Player.sharedState(
    name: String,
    block: SharedState.() -> Unit
): SharedState {
    return MinestomPlayerWrapper(this).sharedState(name, block)
}

fun Player.getState(
    name: String
): SharedState? {
    return MinestomPlayerWrapper(this).getState(name)
}