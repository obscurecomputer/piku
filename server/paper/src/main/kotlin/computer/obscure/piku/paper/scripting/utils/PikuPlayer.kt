package computer.obscure.piku.paper.scripting.utils

import computer.obscure.piku.core.scripting.server.PlayerStorage
import org.bukkit.entity.Player

val Player.piku
    get() = PlayerStorage.get<Player>(this.uniqueId)!!