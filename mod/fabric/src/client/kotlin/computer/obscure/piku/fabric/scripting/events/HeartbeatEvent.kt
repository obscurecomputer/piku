package computer.obscure.piku.fabric.scripting.events

import computer.obscure.piku.fabric.PikuClient
import computer.obscure.piku.common.scripting.base.LuaEvent

object HeartbeatEvent : LuaEvent() {
    override val id = ""
    override val onClientReceive: () -> Unit
        get() = {
            PikuClient.engine.events.send(id, true)
        }
}