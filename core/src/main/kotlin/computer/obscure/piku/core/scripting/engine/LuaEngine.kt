package computer.obscure.piku.core.scripting.engine

import computer.obscure.piku.core.scripting.api.*
import computer.obscure.piku.core.scripting.platform.CoreBlocks
import computer.obscure.piku.core.service.PikuService
import computer.obscure.twine.TwineEngine
import computer.obscure.twine.TwineEnvironment
import computer.obscure.twine.TwineNative
import me.znotchill.kiwi.generated.builders.ColorBuilder
import me.znotchill.kiwi.generated.builders.Vec2Builder
import me.znotchill.kiwi.generated.builders.Vec3Builder

open class LuaEngine : PikuService {
    private var _engine: TwineEngine? = null

    val engine: TwineEngine
        get() = _engine ?: TwineEngine()

    private val registeredNatives: MutableMap<String, TwineNative> = mutableMapOf()
    open val activeScripts: MutableMap<String, String> = mutableMapOf()
    private val engineLock = Any()

    open fun init() {
        shutdown()
        // attach a completely fresh instance of the engine,
        // since LuaStates will persist over disconnects, meaning your game will crash
        // if you try to join another server after in the same session.
        // avoids a JVM crash
        _engine?.close()
        val freshEngine = TwineEngine()
        _engine = freshEngine

        _engine!!.moduleLoader = { name ->
            val script = activeScripts[name]
                ?: activeScripts["$name.luau"]
                ?: activeScripts["$name.lua"]
            script
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
        registerBase(LuaBaseGenerics)
        register(Vec2Builder())
        register(Vec3Builder())
        register(ColorBuilder())
        register(LuaSpacing())
        register(LuaScheduler())
        register(LuaMath())
        register(LuaNoise())
        register(LuaText())

        register(CoreBlocks())
        register(BlockVecBuilder())
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
            result.onFailure {
                throw it
            }
        }
    }
}