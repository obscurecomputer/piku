package computer.obscure.piku.mod.common.scripting.events

import computer.obscure.piku.core.scripting.base.LuaEvent
import computer.obscure.piku.mod.common.Piku

object HeartbeatEvent : LuaEvent() {
    override val id = ""
    override val onClientReceive: () -> Unit
        get() = {
            Piku.engine.events.send(id, true)
        }
}