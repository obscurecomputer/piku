package computer.obscure.piku.core.scripting.engine

import computer.obscure.piku.core.scripting.api.*
import computer.obscure.piku.core.service.PikuService
import computer.obscure.twine.TwineEngine
import computer.obscure.twine.TwineEnvironment
import computer.obscure.twine.TwineLogger
import computer.obscure.twine.TwineNative

abstract class LuaEngine : PikuService {
    private var _engine: TwineEngine? = null

    val engine: TwineEngine
        get() = _engine ?: throw IllegalStateException("Engine not initialized")

    private val registeredNatives: MutableMap<String, TwineNative> = mutableMapOf()
    open val activeScripts: MutableMap<String, String> = mutableMapOf()
    private val engineLock = Any()

    open fun init() {
        shutdown()
        // attach a completely fresh instance of the engine,
        // since LuaStates will persist over disconnects, meaning your game will crash
        // if you try to join another server after in the same session.
        // avoids a JVM crash
//        _engine?.close()
        val freshEngine = TwineEngine()
        _engine = freshEngine

        _engine!!.moduleLoader = { name ->
            activeScripts[name]
                ?: activeScripts["$name.luau"]
                ?: activeScripts["$name.lua"]
        }
        _engine!!.enableRequire()

        registeredNatives.clear()
        registerCommons()
    }

    override fun shutdown() {
        synchronized(engineLock) {
            activeScripts.clear()
            _engine?.close()
            _engine = null
        }
    }

    fun registerCommons() {
        engine.register(LuaVec2())
        engine.register(LuaVec3())
        engine.register(LuaColor())
        engine.register(LuaSpacing())
        engine.register(LuaScheduler())
        engine.register(LuaMath())
        engine.register(LuaText())
    }

    fun register(native: TwineNative) {
        registeredNatives[native.resolvedName] = native
        engine.register(native)
    }

    fun registerBase(native: TwineNative) {
        registeredNatives[native.resolvedName] = native
        engine.setBase(native)
    }

    fun runScript(name: String, content: String) {
        val env = TwineEnvironment()
        env.register(LuaLogger(name))
        env.register("SCRIPT_NAME", name)

        synchronized(engineLock) {
            if (_engine == null || _engine!!.closed) return
            val result = engine.runSafe(name, content, env)
            result.onFailure { throw it }
        }
    }
}