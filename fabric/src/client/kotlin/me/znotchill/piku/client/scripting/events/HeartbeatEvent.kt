package me.znotchill.piku.client.scripting.events

import me.znotchill.piku.client.PikuClient
import me.znotchill.piku.common.scripting.base.LuaEvent
import org.luaj.vm2.LuaValue

object HeartbeatEvent : LuaEvent() {
    override val id = ""
    override val onClientReceive: () -> Unit
        get() = {
            PikuClient.engine.events.send(id, LuaValue.TRUE)
        }
}