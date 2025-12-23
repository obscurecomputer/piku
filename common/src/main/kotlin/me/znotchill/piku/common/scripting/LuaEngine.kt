package me.znotchill.piku.common.scripting

import org.luaj.vm2.Globals

interface LuaEngine {
    val globals: Globals

    fun init()

    fun runScript(name: String, content: String) {
        val globals = globals

        try {
            val chunk = globals.load(content, name)
            chunk.call()
        } catch (e: Exception) {
            println("Lua error: ${e.message}")
        }
    }
}