package computer.obscure.piku.mod.fabric.scripting.engine

import computer.obscure.endergine.SandboxConfig
import computer.obscure.endergine.ScriptRunner
import computer.obscure.piku.core.scripting.engine.LuaEngine
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.scripting.LuaStateManager
import computer.obscure.piku.mod.fabric.scripting.PikuScript
import computer.obscure.piku.mod.fabric.scripting.old.LuaClient
import computer.obscure.piku.mod.fabric.scripting.old.LuaClientEventListener
import computer.obscure.piku.mod.fabric.scripting.old.LuaClientEvents
import computer.obscure.piku.mod.fabric.scripting.old.LuaGame
import computer.obscure.piku.mod.fabric.scripting.old.LuaLevel
import computer.obscure.piku.mod.fabric.scripting.old.LuaScreens
import computer.obscure.piku.mod.fabric.scripting.old.LuaWidgets
import computer.obscure.piku.mod.fabric.scripting.old.controlify.LuaControlify
import computer.obscure.piku.mod.fabric.scripting.old.raycast.LuaRaycast
import computer.obscure.piku.mod.fabric.scripting.old.sound.LuaSound
import computer.obscure.piku.mod.fabric.scripting.old.storage.LuaSessionStorage
import computer.obscure.piku.mod.fabric.scripting.old.ui.LuaEasing
import kotlinx.coroutines.launch
import me.znotchill.kiwi.twine.TwineCompat
import kotlin.script.experimental.api.ResultWithDiagnostics

class ClientLuaEngine : LuaEngine() {
    lateinit var events: LuaClientEvents
        private set

    val config = SandboxConfig {
        extension = "piku"
        classLoader = PikuScript::class.java.classLoader

        allow("computer.obscure.piku.")
        allow("me.znotchill.kiwi.")
        allow("")

        onError = { errors ->
            errors.forEach {
                println("BLOCKED: ${it.blockedClass} ${it.method} ${it.className}")
            }
        }
    }
    val runner = ScriptRunner(config)

    override fun shutdown() {
        PikuClient.info("Engine shut down")
        super.shutdown()
        events.clear()
    }

    override fun init() {
        events = LuaClientEvents()
        super.init()

        events.registerBaseListeners()

        register(LuaGame())
        register(LuaClient())
        register(LuaEasing())
        register(LuaStateManager())
        register(LuaScreens())
        register(LuaWidgets())
        register(LuaRaycast())
        register(LuaLevel())
        register(LuaSessionStorage())
        register(LuaControlify())
        register(LuaSound())
        TwineCompat.bind(twine)
        super.registerCommons()

        val eventListener = LuaClientEventListener()
        eventListener.bus = events
        registerBase(eventListener)
    }

    fun runScript(name: String, content: String) {
        scriptScope.launch {
            println("COMPILING $name")

            val result = runner.compile(name, content)

            if (result is ResultWithDiagnostics.Success) {
                println("COMPILED $name")

                PikuClient.execute {
                    println("EXECUTING $name")

                    try {
                        runner.execute(result.value)
                    } catch (e: Throwable) {
                        PikuClient.error("Script execution failed: ${e.message}")
                    }
                }
            } else {
                println("FAILED TO COMPILE $name")
            }
        }
    }

}