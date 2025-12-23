package me.znotchill.piku.common.scripting.server

import me.znotchill.piku.common.scripting.LuaEngine

interface ServerAPI<T> {
    val engine: LuaEngine
    fun registerEvents()
    fun sendScript(player: T, name: String, content: String)
    fun runScript(name: String, content: String) {
        engine.runScript(name, content)
    }
}