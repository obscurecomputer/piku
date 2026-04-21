package computer.obscure.piku.mod.fabric.scripting.engine

import computer.obscure.piku.core.scripting.engine.LuaEngine
import computer.obscure.piku.core.ui.Anchor
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.scripting.LuaStateManager
import computer.obscure.piku.mod.fabric.scripting.api.LuaClient
import computer.obscure.piku.mod.fabric.scripting.api.LuaClientEventListener
import computer.obscure.piku.mod.fabric.scripting.api.LuaClientEvents
import computer.obscure.piku.mod.fabric.scripting.api.LuaGame
import computer.obscure.piku.mod.fabric.scripting.api.LuaScreens
import computer.obscure.piku.mod.fabric.scripting.api.LuaWidgets
import computer.obscure.piku.mod.fabric.scripting.api.raycast.LuaRaycast
import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaEasing
import computer.obscure.twine.TwineEnum.Companion.toTwineEnum

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

        register(LuaGame())
        register(LuaClient())
        register(Anchor::class.toTwineEnum())
        register(LuaEasing())
        register(LuaStateManager())
        register(LuaScreens())
        register(LuaWidgets())
        register(LuaRaycast())
        super.registerCommons()

        val eventListener = LuaClientEventListener()
        eventListener.bus = events
        registerBase(eventListener)
    }
}