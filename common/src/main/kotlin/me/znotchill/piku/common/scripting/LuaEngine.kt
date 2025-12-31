package me.znotchill.piku.common.scripting

import dev.znci.twine.TwineTable
import dev.znci.twine.nativex.TwineNative
import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.Bit32Lib
import org.luaj.vm2.lib.CoroutineLib
import org.luaj.vm2.lib.PackageLib
import org.luaj.vm2.lib.TableLib
import org.luaj.vm2.lib.jse.JseBaseLib
import org.luaj.vm2.lib.jse.JseMathLib
import org.luaj.vm2.lib.jse.LuajavaLib

interface LuaEngine {
    var globals: Globals
    val registeredTables: MutableMap<String, TwineTable>

    fun init() {
        registeredTables.clear()
        rebuildGlobals()
    }

    fun reset() {
        rebuildGlobals()
        rebuildEvents()
    }

    private fun rebuildGlobals() {
        globals = Globals()

        globals.load(JseBaseLib())
        globals.load(PackageLib())
        globals.load(Bit32Lib())
        globals.load(TableLib())
        globals.load(CoroutineLib())
        globals.load(JseMathLib())
        globals.load(LuajavaLib())

        LoadState.install(globals)
        LuaC.install(globals)

        // re-register natives
        registeredTables.values.forEach {
            globals.set(it.valueName, it.table)
        }
    }

    fun rebuildEvents() {}

    fun register(native: TwineTable) {
        registeredTables[native.valueName] = native
        globals.set(native.valueName, native.table)
    }

    fun runScript(name: String, content: String) {
        try {
            globals.load(content, name).call()
        } catch (e: Exception) {
            println("Lua error in $name: ${e.message}")
        }
    }
}
