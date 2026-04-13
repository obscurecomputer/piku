package computer.obscure.piku.core.scripting.engine

import computer.obscure.piku.core.scripting.api.*
import computer.obscure.piku.core.service.PikuService
import computer.obscure.twine.TwineEngine
import computer.obscure.twine.TwineEnvironment
import computer.obscure.twine.TwineNative

abstract class LuaEngine : PikuService {
    private var _engine: TwineEngine? = null

    val twine: TwineEngine
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
        twine.register(LuaVec2())
        twine.register(LuaVec3())
        twine.register(LuaColor())
        twine.register(LuaSpacing())
        twine.register(LuaScheduler())
        twine.register(LuaMath())
        twine.register(LuaNoise())
        twine.register(LuaText())
    }

    fun register(native: TwineNative) {
        registeredNatives[native.resolvedName] = native
        twine.register(native)
    }

    fun registerBase(native: TwineNative) {
        registeredNatives[native.resolvedName] = native
        twine.setBase(native)
    }

    fun runScript(name: String, content: String) {
        val env = TwineEnvironment()
        env.register(LuaLogger(name))
        env.register("SCRIPT_NAME", name)

        synchronized(engineLock) {
            if (_engine == null || _engine!!.closed) return
            val result = twine.runSafe(name, content, env)
            result.onFailure { throw it }
        }
    }
}