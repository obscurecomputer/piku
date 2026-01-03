package computer.obscure.piku.minestom.scripting

import computer.obscure.piku.common.scripting.LuaEngine
import computer.obscure.piku.common.scripting.api.LuaColor
import computer.obscure.piku.common.scripting.api.LuaVec2
import computer.obscure.piku.minestom.scripting.api.LuaServerEvents

class MinestomLuaEngine : LuaEngine() {
    lateinit var events: LuaServerEvents

    override fun init() {
        super.init()
        events = LuaServerEvents()

        register(LuaVec2())
        register(LuaColor())

        val eventListener = LuaServerEventListener()
        eventListener.bus = events
        registerBase(eventListener)
    }
}

