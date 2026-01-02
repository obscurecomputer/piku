package me.znotchill.piku.common.scripting

import computer.obscure.twine.TwineTable
import computer.obscure.twine.nativex.TwineEngine
import computer.obscure.twine.nativex.TwineNative
import org.luaj.vm2.LoadState
import org.luaj.vm2.LuaError
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.Bit32Lib
import org.luaj.vm2.lib.CoroutineLib
import org.luaj.vm2.lib.PackageLib
import org.luaj.vm2.lib.TableLib
import org.luaj.vm2.lib.jse.JseBaseLib
import org.luaj.vm2.lib.jse.JseMathLib
import org.luaj.vm2.lib.jse.LuajavaLib

abstract class LuaEngine {
    val engine = TwineEngine()
    open val registeredTables: MutableMap<String, TwineTable> = mutableMapOf()
    open val registeredBaseTables: MutableList<TwineNative> = mutableListOf()

    open fun init() {
        engine.clear()
        registeredBaseTables.clear()
        registeredTables.clear()
        rebuildGlobals()
    }

    private fun rebuildGlobals() {
        engine.clear()

        engine.load(JseBaseLib())
        engine.load(PackageLib())
        engine.load(Bit32Lib())
        engine.load(TableLib())
        engine.load(CoroutineLib())
        engine.load(JseMathLib())
        engine.load(LuajavaLib())

        LoadState.install(engine.globals)
        LuaC.install(engine.globals)

        // re-register natives
        registeredTables.values.forEach {
            engine.set(it.valueName, it.table)
        }
        registeredBaseTables.forEach {
            engine.setBase(it)
        }
    }

    open fun rebuildEvents() {}

    fun register(table: TwineTable) {
        registeredTables[table.valueName] = table
        engine.set(table.valueName, table.table)
    }

    fun registerBase(native: TwineNative) {
        registeredBaseTables.add(native)
        engine.setBase(native)
    }

    fun runScript(name: String, content: String) {
        val result = engine.runSafe(name, content)
        if (result.isFailure) {
            throw LuaError(result.exceptionOrNull())
        }
    }
}
