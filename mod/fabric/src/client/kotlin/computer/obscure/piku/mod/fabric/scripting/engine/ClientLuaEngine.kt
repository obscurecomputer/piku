package computer.obscure.piku.mod.fabric.scripting.engine

import computer.obscure.endergine.SandboxConfig
import computer.obscure.endergine.ScriptRunner
import computer.obscure.piku.core.scripting.engine.LuaEngine
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.scripting.PikuScript
import kotlinx.coroutines.launch
import kotlin.script.experimental.api.ResultWithDiagnostics

class ClientLuaEngine : LuaEngine() {
    lateinit var events: ClientEventBus
        private set

    override fun init() {
        events = ClientEventBus()
    }

    val config = SandboxConfig {
        extension = "piku"
        classLoader = PikuScript::class.java.classLoader

        allow("computer.obscure.piku.")
        allow("me.znotchill.kiwi.")
        allow("net.kyori.adventure.")
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