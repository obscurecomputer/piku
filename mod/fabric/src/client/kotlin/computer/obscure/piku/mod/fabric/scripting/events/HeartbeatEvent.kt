package computer.obscure.piku.mod.fabric.scripting.events

import computer.obscure.piku.core.scripting.base.LuaEvent
import computer.obscure.piku.mod.fabric.PikuClient

object HeartbeatEvent : LuaEvent() {
    override val id = ""
    override val onClientReceive: () -> Unit
        get() = {
            PikuClient.engine.events.send(id, mapOf("alive" to true))
        }
}