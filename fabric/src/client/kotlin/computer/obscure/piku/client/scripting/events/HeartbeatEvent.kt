package computer.obscure.piku.client.scripting.events

import computer.obscure.piku.client.PikuClient
import computer.obscure.piku.common.scripting.base.LuaEvent

object HeartbeatEvent : LuaEvent() {
    override val id = ""
    override val onClientReceive: () -> Unit
        get() = {
            PikuClient.engine.events.send(id, true)
        }
}