package computer.obscure.piku.fabric.scripting

import computer.obscure.piku.common.scripting.base.EventBus
import org.luaj.vm2.LuaValue

interface ClientEventBus : EventBus {
    fun send(eventId: String, data: LuaValue)
}