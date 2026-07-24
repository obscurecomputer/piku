package computer.obscure.piku.core.scripting.server

import computer.obscure.piku.core.classes.ScriptSource
import computer.obscure.piku.core.states.SharedState
import computer.obscure.piku.core.states.StateOwner
import java.io.File
import java.net.JarURLConnection
import java.nio.charset.StandardCharsets
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchEvent
import java.util.concurrent.ExecutorService
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

data class ReloadedFile(val file: File, val timestamp: Long)

data class HotReloadListener<T>(
    val players: () -> List<T>,
    val source: ScriptSource,
    val recurse: Boolean = true,
    val debounceMs: Long = 300,
    val onSuccessfulReload: (List<T>, List<ReloadedFile>) -> Unit,
    val eventQueue: MutableList<ReloadedFile> = mutableListOf()
)

data class PendingReload<T>(
    val listener: HotReloadListener<T>,
    val changedFiles: List<ReloadedFile>,
    val expected: MutableSet<T>,
    val completed: MutableSet<T> = mutableSetOf()
)

interface ServerAPI<T> {
    val events: ServerEventBus<T>
    val hotReloadListeners: MutableList<HotReloadListener<T>>
    val hotReloadExecutor: ExecutorService
    val pendingReloads: MutableMap<Long, PendingReload<T>>
    val reloadIdCounter: AtomicLong

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

    fun unloadScripts(player: T, reloadId: Long)
    fun unloadScripts(players: Collection<T>, reloadId: Long) =
        players.forEach { unloadScripts(it, reloadId) }
    fun unloadScripts(players: List<T>, reloadId: Long) =
        players.forEach { unloadScripts(it, reloadId) }

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
                val validPlayers = owners
                    .filter { it != null && it !in exemptList }
                    .filterIsInstance<StateOwner<*>>()
                @Suppress("UNCHECKED_CAST")
                sendState(validPlayers.map { it.owner as T }, state)
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
        listener: HotReloadListener<T>,
        changedFiles: List<ReloadedFile>
    ) {
        val playerList = listener.players.invoke()
        if (playerList.isEmpty()) {
            listener.onSuccessfulReload(playerList, changedFiles)
            return
        }

        val reloadId = reloadIdCounter.incrementAndGet()
        pendingReloads[reloadId] = PendingReload(
            listener = listener,
            changedFiles = changedFiles,
            expected = playerList.toMutableSet()
        )
        unloadScripts(playerList, reloadId)
    }

    fun handleFinishedUnloading(player: T, reloadId: Long) {
        val reload = pendingReloads[reloadId] ?: return
        sendAllScripts(player, reload.listener.source, reload.listener.recurse)
        reload.completed.add(player)

        if (reload.completed.containsAll(reload.expected)) {
            reload.listener.onSuccessfulReload(reload.expected.toList(), reload.changedFiles)
            pendingReloads.remove(reloadId)
        }
    }

    fun hotReload(
        players: () -> List<T>,
        source: ScriptSource,
        recurse: Boolean = true,
        debounceMs: Long = 300,
        onSuccessfulReload: (List<T>, List<ReloadedFile>) -> Unit
    ) {
        val dir = when (source) {
            is ScriptSource.Directory -> source.dir.toPath()
            // jar resources can not be watched
            is ScriptSource.Resource -> return
        }

        val listener = HotReloadListener(
            players = players,
            source = source,
            recurse = recurse,
            debounceMs = debounceMs,
            onSuccessfulReload = onSuccessfulReload,
            eventQueue = mutableListOf()
        )
        hotReloadListeners.add(listener)

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
            val pending = AtomicBoolean(false)

            while (true) {
                val key = watcher.take()
                val watchedDir = key.watchable() as Path

                key.pollEvents().forEach { event ->
                    @Suppress("UNCHECKED_CAST")
                    val relativePath = (event as WatchEvent<Path>).context()
                    val changedFile = watchedDir.resolve(relativePath).toFile()

                    // might not be specific enough, but seems to filter out
                    // some weird temp files on linux in intellij
                    if (changedFile.name.endsWith("~")) return@forEach
                    synchronized(listener.eventQueue) {
                        listener.eventQueue.add(ReloadedFile(changedFile, System.currentTimeMillis()))
                    }
                }

                val now = System.currentTimeMillis()
                if (now - lastReload > debounceMs && pending.compareAndSet(false, true)) {
                    lastReload = now
                    hotReloadExecutor.submit {
                        val batch = synchronized(listener.eventQueue) {
                            val snapshot = listener.eventQueue.toList()
                            listener.eventQueue.clear()
                            snapshot
                        }
                        try {
                            runHotReload(listener, batch)
                        } finally {
                            pending.set(false)
                        }
                    }
                }

                if (!key.reset()) break
            }
        }.apply {
            isDaemon = true
            name = "script-watcher"
            start()
        }
    }

    fun handlePlayerDisconnect(player: T) {
        val toComplete = mutableListOf<Long>()

        pendingReloads.forEach { (id, reload) ->
            reload.expected.remove(player)
            reload.completed.remove(player)
            if (reload.expected.isNotEmpty() && reload.completed.containsAll(reload.expected)) {
                toComplete.add(id)
            } else if (reload.expected.isEmpty()) {
                // everyone left, drop it
                toComplete.add(id)
            }
        }

        toComplete.forEach { id ->
            val reload = pendingReloads.remove(id) ?: return@forEach
            if (reload.completed.isNotEmpty()) {
                reload.listener.onSuccessfulReload(reload.completed.toList(), reload.changedFiles)
            }
        }
    }
}