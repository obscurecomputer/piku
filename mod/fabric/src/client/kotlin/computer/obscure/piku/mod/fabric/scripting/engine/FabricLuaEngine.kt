package computer.obscure.piku.mod.fabric.scripting.engine

import computer.obscure.piku.core.scripting.engine.LuaEngine
import computer.obscure.piku.mod.fabric.scripting.LuaStateManager
import computer.obscure.piku.mod.fabric.scripting.api.LuaClient
import computer.obscure.piku.mod.fabric.scripting.api.LuaClientEventListener
import computer.obscure.piku.mod.fabric.scripting.api.LuaClientEvents
import computer.obscure.piku.mod.fabric.scripting.api.LuaGame
import computer.obscure.piku.mod.fabric.scripting.api.LuaScreens
import computer.obscure.piku.mod.fabric.scripting.api.LuaWidgets
import computer.obscure.piku.mod.fabric.scripting.api.enums.LuaUIAnchor
import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaEasing

class ClientLuaEngine : LuaEngine() {
    lateinit var events: LuaClientEvents
        private set

    override fun shutdown() {
        super.shutdown()
        events.clear()
    }

    override fun init() {
        super.init()
        events = LuaClientEvents()

        register(LuaGame())
        register(LuaClient())
        register(LuaUIAnchor())
        register(LuaEasing())
        register(LuaStateManager())
        register(LuaScreens())
        register(LuaWidgets())
        super.registerCommons()

        val eventListener = LuaClientEventListener()
        eventListener.bus = events
        registerBase(eventListener)
    }
}