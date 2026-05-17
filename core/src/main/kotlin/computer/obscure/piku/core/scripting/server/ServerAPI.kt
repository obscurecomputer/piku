package computer.obscure.piku.core.scripting.server

import computer.obscure.piku.core.classes.ScriptSource
import computer.obscure.piku.core.scripting.base.Player
import computer.obscure.piku.core.scripting.engine.LuaEngine
import computer.obscure.piku.core.states.SharedState
import java.io.File
import java.net.JarURLConnection
import java.nio.charset.StandardCharsets
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.StandardWatchEventKinds

data class HotReloadListener<T>(
    val players: () -> List<T>,
    val source: ScriptSource,
    val recurse: Boolean = true,
    val debounceMs: Long = 300,
    val onSuccessfulReload: (List<T>) -> Unit
)

interface ServerAPI<T> {
    val events: ServerEventBus<T>
    val hotReloadListeners: MutableList<HotReloadListener<T>>

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

    fun unloadScripts(player: T)
    fun unloadScripts(players: Collection<T>) =
        players.forEach { unloadScripts(it) }
    fun unloadScripts(players: List<T>) =
        players.forEach { unloadScripts(it) }

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

                    file.isFile && file.extension in setOf("lua", "luau") ->
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
        players: List<T>,
        source: ScriptSource,
        recurse: Boolean = true
    ) = players.forEach { sendAllScripts(it, source, recurse) }
    fun sendAllScripts(
        players: Collection<T>,
        source: ScriptSource,
        recurse: Boolean = true
    ) = players.forEach { sendAllScripts(it, source, recurse) }
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

    private fun runHotReload(
        players: () -> List<T>,
        source: ScriptSource,
        recurse: Boolean = true,
        onSuccessfulReload: (List<T>) -> Unit
    ) {
        val playerList = players.invoke()
        unloadScripts(playerList)
        sendAllScripts(playerList, source, recurse)
        onSuccessfulReload(playerList)
    }

    fun hotReload(
        players: () -> List<T>,
        source: ScriptSource,
        recurse: Boolean = true,
        debounceMs: Long = 300,
        onSuccessfulReload: (List<T>) -> Unit
    ) {
        val dir = when (source) {
            is ScriptSource.Directory -> source.dir.toPath()
            // jar resources can not be watched
            is ScriptSource.Resource -> return
        }

        hotReloadListeners.add(
            HotReloadListener(
                players = players,
                source = source,
                recurse = recurse,
                debounceMs = debounceMs,
                onSuccessfulReload = onSuccessfulReload
            )
        )

        Thread {
            val watcher = FileSystems.getDefault().newWatchService()

            if (recurse) {
                Files.walk(dir)
                    .filter { Files.isDirectory(it) }
                    .forEach { it.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY) }
            } else {
                dir.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY)
            }

            var lastReload = 0L

            while (true) {
                val key = watcher.take()
                key.pollEvents()

                val now = System.currentTimeMillis()
                if (now - lastReload > debounceMs) {
                    lastReload = now
                    runHotReload(players, source, recurse, onSuccessfulReload)
                }

                // dir no longer accessible
                if (!key.reset()) break
            }
        }.apply {
            isDaemon = true
            name = "script-watcher"
            start()
        }
    }
}