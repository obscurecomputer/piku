package computer.obscure.piku.fabric.scripting.engine

import computer.obscure.piku.fabric.scripting.api.LuaClient
import computer.obscure.piku.fabric.scripting.api.LuaClientEventListener
import computer.obscure.piku.fabric.scripting.api.LuaClientEvents
import computer.obscure.piku.fabric.scripting.api.LuaGame
import computer.obscure.piku.fabric.scripting.api.enums.LuaUIAnchor
import computer.obscure.piku.fabric.scripting.api.ui.LuaEasing
import computer.obscure.piku.common.scripting.engine.LuaEngine

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
        register(LuaClient())
        register(LuaUIAnchor())
        register(LuaEasing())
        super.registerCommons()

        val eventListener = LuaClientEventListener()
        eventListener.bus = events
        registerBase(eventListener)
    }
}