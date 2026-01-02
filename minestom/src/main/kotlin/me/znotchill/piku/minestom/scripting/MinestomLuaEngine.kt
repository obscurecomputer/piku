package me.znotchill.piku.minestom.scripting

import me.znotchill.piku.common.scripting.LuaEngine
import me.znotchill.piku.common.scripting.api.LuaColor
import me.znotchill.piku.common.scripting.api.LuaEventListener
import me.znotchill.piku.common.scripting.api.LuaVec2
import me.znotchill.piku.minestom.scripting.api.LuaServerEvents

class MinestomLuaEngine : LuaEngine() {
    lateinit var events: LuaServerEvents

    override fun init() {
        super.init()
        events = LuaServerEvents()

        register(LuaVec2())
        register(LuaColor())

        val eventListener = LuaEventListener()
        eventListener.bus = events
        registerBase(eventListener)
    }
}

