package computer.obscure.piku.mod.common.scripting

import computer.obscure.piku.core.scripting.base.EventBus
import org.luaj.vm2.LuaValue

interface ClientEventBus : EventBus {
    fun send(eventId: String, data: LuaValue)
}