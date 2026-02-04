package computer.obscure.piku.minestom.scripting

import computer.obscure.piku.core.scripting.base.EventBus
import computer.obscure.piku.core.scripting.base.Player
import org.luaj.vm2.LuaValue

interface ServerEventBus : EventBus {
    fun send(player: Player, eventId: String, data: LuaValue)
}