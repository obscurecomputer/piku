package computer.obscure.piku.minestom.scripting

import computer.obscure.piku.core.scripting.api.LocalScriptEvents
import computer.obscure.piku.core.scripting.api.NetEvents
import computer.obscure.piku.core.scripting.engine.LuaEngine
import computer.obscure.piku.core.scripting.events.EventBus
import computer.obscure.piku.core.scripting.events.NetBus
import computer.obscure.piku.core.scripting.platform.Platform
import computer.obscure.piku.core.scripting.server.*
import computer.obscure.piku.core.states.SharedState
import computer.obscure.piku.core.utils.NetworkSerializer
import computer.obscure.piku.core.utils.asString
import computer.obscure.piku.core.utils.readString
import computer.obscure.piku.core.utils.toJson
import computer.obscure.piku.minestom.scripting.platform.MinestomPlayer
import computer.obscure.twine.TwineError
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import me.znotchill.kiwi.blossom.extensions.addListener
import me.znotchill.kiwi.blossom.server.BlossomServer
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerLoadedEvent
import net.minestom.server.event.player.PlayerPluginMessageEvent
import net.minestom.server.network.NetworkBuffer
import net.minestom.server.network.packet.server.common.PluginMessagePacket
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicLong

class MinestomAPI(val server: BlossomServer) : ServerAPI<Player> {
    override val hotReloadListeners: MutableList<HotReloadListener<Player>> = mutableListOf()

    override val hotReloadExecutor: ExecutorService =
        Executors.newSingleThreadExecutor { r ->
            Thread(r, "script-reloader").apply { isDaemon = true }
        }

    override val pendingReloads: MutableMap<Long, PendingReload<Player>> = ConcurrentHashMap()
    override val reloadIdCounter = AtomicLong(0)

    override var serverEvents = EventBus()
    override var clientEvents = createClientEvents()

    private lateinit var serverScriptEvents: LocalScriptEvents
    private lateinit var clientScriptEvents: NetEvents<Player>

    override var engine = LuaEngine()

    private fun createClientEvents(): NetBus<Player> {
        return NetBus { channel, data, player ->
            sendData(player, channel, data ?: emptyMap<String, Any?>())
        }
    }

    private fun bindEvents() {
        serverScriptEvents = LocalScriptEvents(serverEvents)

        clientScriptEvents = NetEvents(
            bus = clientEvents,
            wrap = { MinestomPlayer(it) },
            unwrap = {
                (it as? MinestomPlayer)?.player
                    ?: throw TwineError("net.emit needs a player")
            }
        )

        engine.register(serverScriptEvents)
        engine.register(clientScriptEvents)
    }

    fun resetEngine() {
        serverScriptEvents.disconnectAll()
        clientScriptEvents.disconnectAll()
        engine.shutdown()

        serverEvents = EventBus()
        clientEvents = createClientEvents()
        engine = LuaEngine()

        bindEvents()
    }

    override fun registerEvents() {
        SharedStateManager.piku = this
        engine.init()
        bindEvents()
        server.eventHandler.addListener<PlayerPluginMessageEvent> { event ->
            when (event.identifier) {
                "piku:send_data" -> {
                    try {
                        val buffer: ByteBuf = Unpooled.wrappedBuffer(event.message)

                        val eventId = buffer.readString()
                        val data = buffer.readString()

                        buffer.release()

                        val decoded = NetworkSerializer.toKotlin(data)
                        clientEvents.receive(eventId, decoded, event.player)
                    } catch (e: Exception) {
                        error("Failed to decode: ${e.message}")
                    }
                }
                "piku:send_state" -> {
                    try {
                        val buffer: ByteBuf = Unpooled.wrappedBuffer(event.message)

                        val rawInternalId = buffer.readString()
                        val value = buffer.readString()

                        buffer.release()

                        val internalId = UUID.fromString(rawInternalId)

                        val state = SharedStateManager.getState(internalId)
                            ?: throw NullPointerException("State with ID '$internalId' does not exist!")

                        if (!state.clientModifiable)
                            throw IllegalAccessException("SharedState '${state.name}' is not client modifiable!")

                        state.set(
                            NetworkSerializer.toKotlin(value)!!,
                            exemptSyncOwners = listOf(event.player)
                        )
                    } catch (e: Exception) {
                        error("Failed to decode: ${e.message}")
                    }
                }
                "piku:finished_unloading_scripts" -> {
                    try {
                        val buf = NetworkBuffer.wrap(event.message, 0, 0)
                        val reloadId = buf.read(NetworkBuffer.VAR_LONG)

                        handleFinishedUnloading(event.player, reloadId)
                    } catch (e: Exception) {
                        error("Failed to decode: ${e.message}")
                    }
                }
            }
        }

        clientEvents.listen("piku.brand") { _, player ->
            val p = PlayerStorage.get<Player>(player.uuid)
                ?: throw NullPointerException("Can not access non-existent player storage!")
            p.usingPiku = true
        }

        server.eventHandler.addListener<PlayerLoadedEvent> { event ->
            PlayerStorage.add(event.player.uuid, PikuPlayer)
            sendData(event.player, "piku.brand", true)
        }

        server.eventHandler.addListener<PlayerDisconnectEvent> { event ->
            PlayerStorage.remove(event.player.uuid)
            handlePlayerDisconnect(event.player)
        }
    }

    override fun sendData(player: Player, eventId: String, data: Any) {
        val jsonString = data.toJson().asString()

        val bytes = NetworkBuffer.makeArray { buffer ->
            buffer.write(NetworkBuffer.STRING, eventId)
            buffer.write(NetworkBuffer.STRING, jsonString)
        }

        player.sendPacket(PluginMessagePacket("piku:receive_data", bytes))
    }

    override fun sendState(player: Player, state: SharedState) {
        val serializedData = state.value.toJson().asString()

        val bytes = NetworkBuffer.makeArray { buffer ->
            buffer.write(NetworkBuffer.STRING, state.internalId.toString())
            buffer.write(NetworkBuffer.STRING, state.name)
            buffer.write(NetworkBuffer.STRING, serializedData)
            buffer.write(NetworkBuffer.BOOLEAN, state.clientModifiable)
        }

        player.sendPacket(
            PluginMessagePacket("piku:receive_state", bytes)
        )
    }

    override fun sendScript(player: Player, name: String, content: String) {
        val bytes = NetworkBuffer.makeArray { buffer ->
            buffer.write(NetworkBuffer.STRING, name)
            buffer.write(NetworkBuffer.STRING, content)
        }

        player.sendPacket(
            PluginMessagePacket("piku:receive_script", bytes)
        )
    }

    override fun unloadScripts(player: Player, reloadId: Long) {
        val bytes = NetworkBuffer.makeArray { buffer ->
            buffer.write(NetworkBuffer.VAR_LONG, reloadId)
        }

        player.sendPacket(
            PluginMessagePacket("piku:unload_scripts", bytes)
        )
    }
}