package computer.obscure.piku.mod.fabric.scripting.engine

import computer.obscure.piku.core.scripting.engine.LuaEngine
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.scripting.LuaStateManager
import computer.obscure.piku.mod.fabric.scripting.api.LuaClient
import computer.obscure.piku.mod.fabric.scripting.api.LuaClientEventListener
import computer.obscure.piku.mod.fabric.scripting.api.LuaClientEvents
import computer.obscure.piku.mod.fabric.scripting.api.LuaGame
import computer.obscure.piku.mod.fabric.scripting.api.LuaLevel
import computer.obscure.piku.mod.fabric.scripting.api.LuaScreens
import computer.obscure.piku.mod.fabric.scripting.api.LuaWidgets
import computer.obscure.piku.mod.fabric.scripting.api.raycast.LuaRaycast
import computer.obscure.piku.mod.fabric.scripting.api.storage.LuaSessionStorage
import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaEasing

class ClientLuaEngine : LuaEngine() {
    lateinit var events: LuaClientEvents
        private set

    override fun shutdown() {
        PikuClient.info("Engine shut down")
        super.shutdown()
        events.clear()
    }

    override fun init() {
        events = LuaClientEvents()
        super.init()

        events.registerBaseListeners()

        register(LuaGame())
        register(LuaClient())
        register(LuaEasing())
        register(LuaStateManager())
        register(LuaScreens())
        register(LuaWidgets())
        register(LuaRaycast())
        register(LuaLevel())
        register(LuaSessionStorage())
        super.registerCommons()

        val eventListener = LuaClientEventListener()
        eventListener.bus = events
        registerBase(eventListener)
    }
}