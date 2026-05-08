package computer.obscure.piku.core.scripting.server

import java.util.UUID

object PlayerStorage {
    private val players: MutableMap<UUID, PikuPlayer<*>> = mutableMapOf()

    fun <P> add(uuid: UUID, player: P) {
        players[uuid] = PikuPlayer.create(player)
    }

    fun remove(uuid: UUID) {
        players.remove(uuid)
    }

    @Suppress("UNCHECKED_CAST")
    fun <P> get(uuid: UUID): PikuPlayer<P>? = players[uuid] as? PikuPlayer<P>

    fun getAll(): List<PikuPlayer<*>> = players.values.toList()

    fun contains(uuid: UUID): Boolean = players.containsKey(uuid)
}