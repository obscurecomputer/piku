package me.znotchill.piku.client.scripting

import LuaClientEvents
import me.znotchill.piku.common.scripting.LuaEngine
import me.znotchill.piku.common.scripting.base.registerListen
import org.luaj.vm2.Globals
import org.luaj.vm2.lib.jse.JsePlatform

class FabricLuaEngine : LuaEngine {
    override lateinit var globals: Globals
        private set

    lateinit var events: LuaClientEvents
        private set

    override fun init() {
        globals = JsePlatform.standardGlobals()
        events = LuaClientEvents()
        events.registerListen(globals)
        events.registerSend(globals)
    }
}