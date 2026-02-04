package computer.obscure.piku.common.scripting.base

import org.luaj.vm2.LuaValue

interface EventBus {
    fun listen(eventId: String, callback: (LuaValue) -> Unit)
    fun fire(eventId: String, data: LuaValue)
}