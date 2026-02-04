package computer.obscure.piku.core.scripting.engine

import computer.obscure.piku.core.scripting.api.LuaColor
import computer.obscure.piku.core.scripting.api.LuaLogger
import computer.obscure.piku.core.scripting.api.LuaMath
import computer.obscure.piku.core.scripting.api.LuaScheduler
import computer.obscure.piku.core.scripting.api.LuaSpacing
import computer.obscure.piku.core.scripting.api.LuaText
import computer.obscure.piku.core.scripting.api.LuaVec2
import computer.obscure.piku.core.scripting.api.LuaVec3
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
    open val activeScripts: MutableMap<String, String> = mutableMapOf()

    open var resourceFinder = EngineResourceFinder(activeScripts)

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

        engine.globals.finder = resourceFinder

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

    fun registerCommons() {
        register(LuaVec2())
        register(LuaVec3())
        register(LuaColor())
        register(LuaSpacing())
        register(LuaScheduler())
        registerBase(LuaMath())
        register(LuaText())
    }

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

        // Allocate a new LuaLogger to this script
        register(LuaLogger(content))

        if (result.isFailure) {
            throw LuaError(result.exceptionOrNull())
        }
    }
}