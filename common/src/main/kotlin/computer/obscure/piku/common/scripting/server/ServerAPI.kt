package computer.obscure.piku.common.scripting.server

import computer.obscure.piku.common.scripting.LuaEngine
import org.luaj.vm2.LuaValue
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

interface ServerAPI<T> {
    val engine: LuaEngine

    fun registerEvents()

    fun sendData(player: T, eventId: String, data: Any)

    fun sendScript(player: T, name: String, content: String)

    fun sendAllScripts(
        player: T,
        resourceDirectory: String,
        recurse: Boolean = true
    ) {
        forEachScript(resourceDirectory, recurse) { name, content ->
            sendScript(player, name, content)
        }
    }

    fun runAllScripts(
        resourceDirectory: String,
        recurse: Boolean = true
    ) {
        forEachScript(resourceDirectory, recurse) { name, content ->
            engine.runScript(name, content)
        }
    }

    private fun forEachScript(
        resourceDirectory: String,
        recurse: Boolean,
        consumer: (name: String, content: String) -> Unit
    ) {
        require(!resourceDirectory.startsWith("/")) {
            "Forbidden leading forward slash!"
        }

        val classLoader = Thread.currentThread().contextClassLoader
        val url = classLoader.getResource(resourceDirectory)
            ?: error("Resource directory not found: $resourceDirectory")

        val dir = File(url.toURI())

        walkDirectory(
            dir = dir,
            basePath = resourceDirectory,
            recurse = recurse,
            consumer = consumer
        )
    }

    private fun walkDirectory(
        dir: File,
        basePath: String,
        recurse: Boolean,
        consumer: (name: String, content: String) -> Unit
    ) {
        dir.listFiles()?.forEach { file ->
            when {
                file.isDirectory && recurse -> {
                    walkDirectory(
                        dir = file,
                        basePath = "$basePath/${file.name}",
                        recurse = recurse,
                        consumer = consumer
                    )
                }

                file.isFile -> {
                    val content = file.readText(StandardCharsets.UTF_8)
                    consumer("$basePath/${file.name}", content)
                }
            }
        }
    }


    fun runScript(name: String, content: String) {
        engine.runScript(name, content)
    }

    fun runScript(name: String, content: List<String>) {
        engine.runScript(name, content.joinToString("\n"))
    }

    fun runScriptFromResource(name: String) {
        runScript(name, getScriptFromResource(name))
    }

    fun getScriptFromResource(name: String): List<String> {
        val inputStream =
            Thread.currentThread().contextClassLoader.getResourceAsStream(name)
                ?: error("Resource not found: $name")

        return BufferedReader(
            InputStreamReader(inputStream, StandardCharsets.UTF_8)
        ).readLines()
    }

    fun getPlayerUD(player: T): LuaValue
}