package computer.obscure.piku.paper.scripting.states

import computer.obscure.piku.core.states.StateOwner
import org.bukkit.entity.Player

class PaperPlayerWrapper(override val owner: Player) : StateOwner<Player>