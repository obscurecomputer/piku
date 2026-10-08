package computer.obscure.piku.mod.fabric.scripting.engine

import computer.obscure.piku.core.scripting.engine.LuaEngine
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.scripting.LuaStateManager
import computer.obscure.piku.mod.fabric.scripting.api.client.LuaClient
import computer.obscure.piku.mod.fabric.scripting.api.events.LuaClientEventListener
import computer.obscure.piku.mod.fabric.scripting.api.events.LuaClientEvents
import computer.obscure.piku.mod.fabric.scripting.api.client.LuaGame
import computer.obscure.piku.mod.fabric.scripting.api.client.LuaLevel
import computer.obscure.piku.mod.fabric.scripting.api.screen.LuaScreens
import computer.obscure.piku.mod.fabric.scripting.api.screen.LuaWidgets
import computer.obscure.piku.mod.fabric.scripting.api.controlify.LuaControlify
import computer.obscure.piku.mod.fabric.scripting.api.raycast.LuaRaycast
import computer.obscure.piku.mod.fabric.scripting.api.sound.LuaSound
import computer.obscure.piku.mod.fabric.scripting.api.storage.LuaSessionStorage
import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaEasing
import me.znotchill.kiwi.twine.TwineCompat

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
        register(LuaControlify())
        register(LuaSound())
        TwineCompat.bind(twine)
        super.registerCommons()

        val eventListener = LuaClientEventListener()
        eventListener.bus = events
        registerBase(eventListener)
    }
}