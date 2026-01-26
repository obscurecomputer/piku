package computer.obscure.piku.common.scripting.server

import computer.obscure.piku.common.classes.ScriptSource
import computer.obscure.piku.common.scripting.engine.LuaEngine
import org.luaj.vm2.LuaValue
import java.io.File
import java.net.JarURLConnection
import java.nio.charset.StandardCharsets

interface ServerAPI<T> {
    val engine: LuaEngine

    fun registerEvents()

    fun sendData(player: T, eventId: String, data: Any)

    fun sendScript(player: T, name: String, content: String)

    private fun forEachFileScript(
        root: File,
        recurse: Boolean,
        consumer: (name: String, content: String) -> Unit
    ) {
        fun walk(dir: File, prefix: String) {
            dir.listFiles()?.forEach { file ->
                when {
                    file.isDirectory && recurse ->
                        walk(file, "$prefix${file.name}/")

                    file.isFile ->
                        consumer(
                            "$prefix${file.name}",
                            file.readText(StandardCharsets.UTF_8)
                        )
                }
            }
        }

        require(root.isDirectory) { "Not a directory: $root" }
        walk(root, "")
    }

    private fun forEachResourceScript(
        resourcePath: String,
        recurse: Boolean,
        consumer: (name: String, content: String) -> Unit
    ) {
        val loader = Thread.currentThread().contextClassLoader
        val url = loader.getResource(resourcePath)
            ?: error("Resource directory not found: $resourcePath")

        when (url.protocol) {
            "file" -> {
                forEachFileScript(File(url.toURI()), recurse, consumer)
            }

            "jar" -> {
                val conn = url.openConnection() as JarURLConnection
                val jar = conn.jarFile
                val prefix = conn.entryName.trimEnd('/') + "/"

                jar.entries().asSequence()
                    .filter { !it.isDirectory }
                    .filter { it.name.startsWith(prefix) }
                    .filter {
                        recurse || !it.name.removePrefix(prefix).contains("/")
                    }
                    .forEach { entry ->
                        jar.getInputStream(entry).use { stream ->
                            consumer(
                                entry.name,
                                stream.bufferedReader(StandardCharsets.UTF_8).readText()
                            )
                        }
                    }
            }

            else -> error("Unsupported protocol: ${url.protocol}")
        }
    }

    fun sendAllScripts(
        player: T,
        source: ScriptSource,
        recurse: Boolean = true
    ) {
        when (source) {
            is ScriptSource.Directory ->
                forEachFileScript(source.dir, recurse) { n, c ->
                    sendScript(player, n, c)
                }

            is ScriptSource.Resource ->
                forEachResourceScript(source.path, recurse) { n, c ->
                    sendScript(player, n, c)
                }
        }

        sendScript(player, "END_OF_SCRIPT_LOADING", "")
    }

    fun runAllScripts(
        source: ScriptSource,
        recurse: Boolean = true
    ) {
        when (source) {
            is ScriptSource.Directory ->
                forEachFileScript(source.dir, recurse) { name, content ->
                    engine.runScript(name, content)
                }

            is ScriptSource.Resource ->
                forEachResourceScript(source.path, recurse) { name, content ->
                    engine.runScript(name, content)
                }
        }
    }

    fun getPlayerUD(player: T): LuaValue
}