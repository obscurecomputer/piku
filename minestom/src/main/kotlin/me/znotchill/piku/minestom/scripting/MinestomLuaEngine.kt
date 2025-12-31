package me.znotchill.piku.minestom.scripting

import dev.znci.twine.TwineTable
import dev.znci.twine.nativex.TwineNative
import me.znotchill.piku.common.scripting.LuaEngine
import me.znotchill.piku.common.scripting.base.registerListen
import me.znotchill.piku.common.scripting.api.PlayerBindings
import me.znotchill.piku.minestom.scripting.api.LuaServerEvents
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.Bit32Lib
import org.luaj.vm2.lib.CoroutineLib
import org.luaj.vm2.lib.PackageLib
import org.luaj.vm2.lib.TableLib
import org.luaj.vm2.lib.jse.JseBaseLib
import org.luaj.vm2.lib.jse.JseMathLib
import org.luaj.vm2.lib.jse.JsePlatform
import org.luaj.vm2.lib.jse.LuajavaLib

class MinestomLuaEngine : LuaEngine {
    override lateinit var globals: Globals

    lateinit var events: LuaServerEvents
        private set

    override var registeredTables: MutableMap<String, TwineTable> = mutableMapOf()

    override fun init() {
        super.init()

        events = LuaServerEvents()

        events.registerListen(globals)
        events.registerSend(globals)
    }
}

