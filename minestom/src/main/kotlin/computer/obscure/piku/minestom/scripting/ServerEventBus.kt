package computer.obscure.piku.minestom.scripting

import computer.obscure.piku.common.scripting.base.EventBus
import computer.obscure.piku.common.scripting.base.Player
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.ThreeArgFunction

interface ServerEventBus : EventBus {
    fun send(player: Player, eventId: String, data: LuaValue)
}

fun ServerEventBus.registerSend(globals: Globals) {
    globals["send"] = object : ThreeArgFunction() {
        override fun call(playerArg: LuaValue, eventArg: LuaValue, dataArg: LuaValue): LuaValue {

            val player = playerArg.touserdata() as? Player
                ?: error("send(player, ...) requires a Player")

            send(player as Player, eventArg.checkjstring(), dataArg)
            return NIL
        }
    }
}
