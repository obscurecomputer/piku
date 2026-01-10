package computer.obscure.piku.client.scripting.engine

import computer.obscure.piku.client.scripting.api.LuaClient
import computer.obscure.piku.client.scripting.api.LuaClientEventListener
import computer.obscure.piku.client.scripting.api.LuaClientEvents
import computer.obscure.piku.client.scripting.api.LuaGame
import computer.obscure.piku.client.scripting.api.enums.LuaUIAnchor
import computer.obscure.piku.client.scripting.api.ui.LuaEasing
import computer.obscure.piku.common.scripting.LuaEngine
import computer.obscure.piku.common.scripting.api.LuaColor
import computer.obscure.piku.common.scripting.api.LuaVec2

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
        register(LuaEasing())

        val eventListener = LuaClientEventListener()
        eventListener.bus = events
        registerBase(eventListener)
    }
}