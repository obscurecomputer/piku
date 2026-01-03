package computer.obscure.piku.client.scripting

import computer.obscure.piku.common.scripting.base.EventBus
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

interface ClientEventBus : EventBus {
    fun send(eventId: String, data: LuaValue)
}

fun ClientEventBus.registerSend(globals: Globals) {
    globals["send"] = object : TwoArgFunction() {
        override fun call(eventArg: LuaValue, dataArg: LuaValue): LuaValue {
            send(eventArg.checkjstring(), dataArg)
            return NIL
        }
    }
}
