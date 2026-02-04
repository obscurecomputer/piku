package computer.obscure.piku.mod.common.scripting.engine

import computer.obscure.piku.core.scripting.engine.LuaEngine
import computer.obscure.piku.mod.common.scripting.api.LuaClient
import computer.obscure.piku.mod.common.scripting.api.LuaClientEventListener
import computer.obscure.piku.mod.common.scripting.api.LuaClientEvents
import computer.obscure.piku.mod.common.scripting.api.LuaGame
import computer.obscure.piku.mod.common.scripting.api.enums.LuaUIAnchor
import computer.obscure.piku.mod.common.scripting.api.ui.LuaEasing

class ClientLuaEngine : LuaEngine() {
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