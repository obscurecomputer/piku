package me.znotchill.piku.client.scripting

import me.znotchill.piku.client.scripting.api.LuaClient
import me.znotchill.piku.client.scripting.api.LuaClientEventListener
import me.znotchill.piku.client.scripting.api.LuaClientEvents
import me.znotchill.piku.client.scripting.api.LuaGame
import me.znotchill.piku.client.scripting.api.enums.LuaUIAnchor
import me.znotchill.piku.common.scripting.LuaEngine
import me.znotchill.piku.common.scripting.api.LuaColor
import me.znotchill.piku.common.scripting.api.LuaVec2

class FabricLuaEngine : LuaEngine() {
    lateinit var events: LuaClientEvents
        private set

    fun shutdown() {
        events.clear()
    }

    override fun init() {
        super.init()
        events = LuaClientEvents()

        register(LuaGame())
        register(LuaUIAnchor())
        register(LuaVec2())
        register(LuaColor())
        register(LuaClient())

        val eventListener = LuaClientEventListener()
        eventListener.bus = events
        registerBase(eventListener)
    }
}