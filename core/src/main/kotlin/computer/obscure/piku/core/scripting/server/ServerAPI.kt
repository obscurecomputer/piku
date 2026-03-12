package computer.obscure.piku.core.scripting.server

import computer.obscure.piku.core.classes.ScriptSource
import computer.obscure.piku.core.scripting.engine.LuaEngine
import computer.obscure.piku.core.states.SharedState
import org.luaj.vm2.LuaValue
import java.io.File
import java.net.JarURLConnection
import java.nio.charset.StandardCharsets

interface ServerAPI<T> {
    val engine: LuaEngine

    fun registerEvents()

    fun sendData(player: T, eventId: String, data: Any)
    fun sendData(players: Collection<T>, eventId: String, data: Any) =
        players.forEach { sendData(it, eventId, data) }
    fun sendData(players: List<T>, eventId: String, data: Any) =
        players.forEach { sendData(it, eventId, data) }

    fun sendState(player: T, state: SharedState)
    fun sendState(players: Collection<T>, state: SharedState) =
        players.forEach { sendState(it, state) }
    fun sendState(players: List<T>, state: SharedState) =
        players.forEach { sendState(it, state) }

    fun syncStateToOwners(
        owners: Any,
        state: SharedState,
        exemptSyncOwners: Any = listOf<Any>()
    ) {
        val exemptList = when (exemptSyncOwners) {
            is List<*> -> exemptSyncOwners
            else -> listOf(exemptSyncOwners)
        }

        when (owners) {
            is List<*> -> {
                val validPlayers = owners.filter { it != null && it !in exemptList }
                @Suppress("UNCHECKED_CAST")
                sendState(validPlayers as List<T>, state)
            }
            else -> {
                @Suppress("UNCHECKED_CAST")
                val player = owners as? T

                if (player != null && player !in exemptList) {
                    sendState(player, state)
                }
            }
        }
    }

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
}