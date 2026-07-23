package computer.obscure.piku.minestom.scripting.states

import computer.obscure.piku.core.states.StateOwner
import net.minestom.server.entity.Player

class MinestomPlayerWrapper(override val owner: Player) : StateOwner<Player>