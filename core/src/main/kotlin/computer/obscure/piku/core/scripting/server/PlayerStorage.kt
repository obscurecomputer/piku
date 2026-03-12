package computer.obscure.piku.core.scripting.server

import computer.obscure.piku.core.scripting.base.Player
import java.util.UUID

object PlayerStorage {
    private val players: MutableMap<UUID, Player> = mutableMapOf()

    fun addPlayer(player: Player) {
        players[UUID.fromString(player.uuid)] = player
    }
}