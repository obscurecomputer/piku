package me.znotchill.piku.client.scripting

import LuaClientEvents
import dev.znci.twine.TwineTable
import dev.znci.twine.nativex.TwineNative
import me.znotchill.piku.client.scripting.api.LuaClient
import me.znotchill.piku.client.scripting.api.LuaGame
import me.znotchill.piku.client.scripting.api.enums.LuaUIAnchor
import me.znotchill.piku.common.scripting.LuaEngine
import me.znotchill.piku.common.scripting.api.LuaColor
import me.znotchill.piku.common.scripting.api.LuaVec2
import me.znotchill.piku.common.scripting.base.registerListen
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.JsePlatform

class FabricLuaEngine : LuaEngine {
    override lateinit var globals: Globals
    override var registeredTables: MutableMap<String, TwineTable> = mutableMapOf()

    lateinit var events: LuaClientEvents
        private set

    override fun init() {
        super.init()

        register(LuaGame())
        register(LuaUIAnchor())
        register(LuaVec2())
        register(LuaColor())
    }

    override fun rebuildEvents() {
        events = LuaClientEvents()
        events.registerListen(globals)
        events.registerSend(globals)
    }
}