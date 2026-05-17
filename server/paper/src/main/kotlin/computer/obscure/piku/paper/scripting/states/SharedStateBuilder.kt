package computer.obscure.piku.paper.scripting.states

import computer.obscure.piku.core.states.SharedState
import computer.obscure.piku.core.states.sharedState
import org.bukkit.entity.Player

fun Player.sharedState(
    name: String,
    block: SharedState.() -> Unit
): SharedState {
    return PaperPlayerWrapper(this).sharedState(name, block)
}