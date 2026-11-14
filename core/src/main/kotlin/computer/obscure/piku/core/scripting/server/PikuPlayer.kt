package computer.obscure.piku.core.scripting.server

// TODO: migrate this into core Player
class PikuPlayer<P>(
    val player: P
) {
    var usingPiku: Boolean = false

    companion object {
        fun <P> create(player: P): PikuPlayer<P> = PikuPlayer(player)
    }
}