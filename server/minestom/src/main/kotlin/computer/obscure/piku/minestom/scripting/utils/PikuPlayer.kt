package computer.obscure.piku.minestom.scripting.utils

import computer.obscure.piku.core.scripting.server.PlayerStorage
import net.minestom.server.entity.Player

val Player.piku
    get() = PlayerStorage.get<Player>(this.uuid)!!