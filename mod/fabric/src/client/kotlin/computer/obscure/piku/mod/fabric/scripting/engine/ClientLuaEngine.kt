package computer.obscure.piku.mod.fabric.scripting.engine

import computer.obscure.endergine.SandboxConfig
import computer.obscure.endergine.ScriptClassLoader
import computer.obscure.endergine.ScriptRunner
import computer.obscure.piku.core.service.PikuService
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.scripting.PikuScript
import computer.obscure.piku.mod.fabric.scripting.PikuScriptConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.script.experimental.api.ResultWithDiagnostics

data class CompiledScript(
    val source: String,
    val compiled: ScriptRunner.CompiledScript,
    var instance: Any? = null
)


class ClientLuaEngine : PikuService {
    lateinit var events: ClientEventBus
        private set

    val scriptScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val loadedScripts = mutableMapOf<String, String>()
    val compiledScripts = mutableMapOf<String, CompiledScript>()
    val activeInstances = mutableMapOf<Class<*>, Any>()

    fun init() {
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
    val runner = ScriptRunner(
        config, PikuScriptConfiguration
    )

    override fun shutdown() {
        PikuClient.info("Engine shut down")
        super.shutdown()
    }

    fun compileScripts() {
        scriptScope.launch {
            println("COMPILING ${loadedScripts.size} SCRIPTS")

            for ((name, content) in loadedScripts) {
                println("COMPILING $name")

                val result = runner.compile(name, content)

                if (result is ResultWithDiagnostics.Success) {
                    compiledScripts[name] = CompiledScript(
                        source = content,
                        compiled = result.value
                    )

                    println("COMPILED $name")
                } else {
                    println("FAILED TO COMPILE $name")
                }
            }

            instantiateScripts()
        }
    }
    private lateinit var scriptLoader: ScriptClassLoader

    fun instantiateScripts() {
        PikuClient.execute {
            try {
                val allClassFiles = compiledScripts.values
                    .flatMap { it.compiled.classFiles.entries }
                    .associate { it.key to it.value }

                scriptLoader = ScriptClassLoader(
                    allClassFiles,
                    config,
                    config.classLoader
                )

                for ((name, script) in compiledScripts) {
                    val scriptClass = scriptLoader.loadClass(
                        script.compiled.scriptClassName
                    )

                    val instance = instantiate(
                        scriptClass,
                        scriptLoader,
                        activeInstances
                    )

                    script.instance = instance

                    println("INSTANTIATED $name -> $instance")
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    fun instantiate(
        scriptClass: Class<*>,
        loader: ClassLoader,
        instances: MutableMap<Class<*>, Any>
    ): Any {
        instances[scriptClass]?.let { return it }

        val constructor = scriptClass.declaredConstructors.first()

        val args = constructor.parameterTypes.map { parameterType ->
            instances[parameterType]
                ?: instantiate(
                    loader.loadClass(parameterType.name),
                    loader,
                    instances
                )
        }.toTypedArray()

        return constructor.newInstance(*args).also {
            instances[scriptClass] = it
        }
    }

    fun runScript(name: String, content: String) {
        scriptScope.launch {
            println("COMPILING $name")

            val result = runner.compile(name, content)

            if (result !is ResultWithDiagnostics.Success) {
                println("FAILED TO COMPILE $name")
                return@launch
            }

            println("COMPILED $name")

            PikuClient.execute {
                try {
                    val compiled = result.value

                    val loader = ScriptClassLoader(
                        compiled.classFiles,
                        config,
                        config.classLoader
                    )

                    val scriptClass = loader.loadClass(
                        compiled.scriptClassName
                    )

                    val instance = instantiate(
                        scriptClass,
                        loader,
                        mutableMapOf()
                    )

                    println("INSTANTIATED $name -> $instance")
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

}