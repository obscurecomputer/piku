package me.znotchill.piku.minestom.scripting

import me.znotchill.piku.common.scripting.LuaEngine
import me.znotchill.piku.common.scripting.base.registerListen
import me.znotchill.piku.minestom.scripting.api.LuaServerEvents
import org.luaj.vm2.Globals
import org.luaj.vm2.lib.jse.JsePlatform

class MinestomLuaEngine : LuaEngine {
    override lateinit var globals: Globals
        private set
    lateinit var events: LuaServerEvents
        private set

    override fun init() {
        globals = JsePlatform.standardGlobals()
        events = LuaServerEvents()
        events.registerListen(globals)
        events.registerSend(globals)
    }
}