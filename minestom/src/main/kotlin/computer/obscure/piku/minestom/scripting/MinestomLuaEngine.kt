package computer.obscure.piku.minestom.scripting

import computer.obscure.piku.common.scripting.LuaEngine
import computer.obscure.piku.minestom.scripting.api.LuaServerEvents

class MinestomLuaEngine : LuaEngine() {
    lateinit var events: LuaServerEvents

    override fun init() {
        super.init()
        events = LuaServerEvents()

        super.registerCommons()

        val eventListener = LuaServerEventListener()
        eventListener.bus = events
        registerBase(eventListener)
    }
}

