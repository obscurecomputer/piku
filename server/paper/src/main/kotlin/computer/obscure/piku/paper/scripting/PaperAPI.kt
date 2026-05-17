package computer.obscure.piku.paper.scripting

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPluginMessage
import computer.obscure.piku.core.classes.ScriptSource
import computer.obscure.piku.core.scripting.server.HotReloadListener
import computer.obscure.piku.core.scripting.server.PikuPlayer
import computer.obscure.piku.core.scripting.server.PlayerStorage
import computer.obscure.piku.core.scripting.server.ServerAPI
import computer.obscure.piku.core.scripting.server.ServerEvents
import computer.obscure.piku.core.scripting.server.SharedStateManager
import computer.obscure.piku.core.states.SharedState
import computer.obscure.piku.core.utils.jsonStringToKotlin
import computer.obscure.piku.core.utils.toJson
import computer.obscure.piku.paper.scripting.utils.piku
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.UUID

class PaperAPI(
    val plugin: JavaPlugin,
    val scriptDir: File = File(plugin.dataFolder, "scripts")
) : ServerAPI<Player>, Listener {
    override val events = ServerEvents<Player>()
    override val hotReloadListeners: MutableList<HotReloadListener<Player>> = mutableListOf()

    override fun registerEvents() {
        SharedStateManager.piku = this

        plugin.server.messenger.registerIncomingPluginChannel(
            plugin, "piku:send_data"
        ) { _, player, message ->
            val input = ByteArrayInputStream(message)
            val eventId = input.readVarIntString()
            val data = input.readVarIntString()
            val luaData = jsonStringToKotlin(data) as? Map<String, Any?> ?: emptyMap()
            events.fire(eventId, luaData, player)
        }

        plugin.server.messenger.registerIncomingPluginChannel(
            plugin, "piku:send_state"
        ) { _, player, message ->
            val input = ByteArrayInputStream(message)
            val rawInternalId = input.readVarIntString()
            val value = input.readVarIntString()
            val internalId = UUID.fromString(rawInternalId)
            val state = SharedStateManager.getState(internalId)
                ?: throw NullPointerException("State '$internalId' not found")
            if (!state.clientModifiable)
                throw IllegalAccessException("State '${state.name}' not modifiable")
            state.set(jsonStringToKotlin(value)!!, exemptSyncOwners = listOf(player))
        }

        plugin.server.messenger.registerOutgoingPluginChannel(plugin, "piku:receive_data")
        plugin.server.messenger.registerOutgoingPluginChannel(plugin, "piku:receive_state")
        plugin.server.messenger.registerOutgoingPluginChannel(plugin, "piku:receive_script")
        plugin.server.messenger.registerOutgoingPluginChannel(plugin, "piku:unload_scripts")

        // must wait for the piku.brand message
        // because paper doesn't have PlayerLoadedEvents like minestom
        events.listen("piku.brand") { _, player ->
            player.piku.usingPiku = true
            sendAllScripts(
                player = player,
                source = ScriptSource.Directory(dir = scriptDir),
                recurse = true
            )
        }

        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        PlayerStorage.add(player.uniqueId, PikuPlayer.create(player))
        sendData(player, "piku.brand", true)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        PlayerStorage.remove(event.player.uniqueId)
    }

    private fun send(player: Player, channel: String, out: ByteArrayOutputStream) {
        val wrapper = WrapperPlayServerPluginMessage(channel, out.toByteArray())
        PacketEvents.getAPI().playerManager.sendPacket(player, wrapper)
    }

    override fun sendScript(player: Player, name: String, content: String) {
        try {
            val out = ByteArrayOutputStream()
            writeVarIntString(out, name)
            writeVarIntString(out, content)
            send(player, "piku:receive_script", out)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun sendData(player: Player, eventId: String, data: Any) {
        val out = ByteArrayOutputStream()
        writeVarIntString(out, eventId)
        writeVarIntString(out, data.toJson())
        send(player, "piku:receive_data", out)
    }

    override fun sendState(player: Player, state: SharedState) {
        val out = ByteArrayOutputStream()
        writeVarIntString(out, state.internalId.toString())
        writeVarIntString(out, state.name)
        writeVarIntString(out, state.value.toJson())
        out.write(if (state.clientModifiable) 1 else 0)
        send(player, "piku:receive_state", out)
    }

    override fun unloadScripts(player: Player) {
        val out = ByteArrayOutputStream()
        out.write(1)
        send(player, "piku:unload_scripts", out)
    }

    private fun writeVarIntString(out: ByteArrayOutputStream, value: String) {
        val bytes = value.toByteArray(Charsets.UTF_8)
        writeVarInt(out, bytes.size)
        out.write(bytes)
    }

    private fun writeVarInt(out: ByteArrayOutputStream, value: Int) {
        var v = value
        while (true) {
            if (v and 0x7F.inv() == 0) { out.write(v); break }
            out.write((v and 0x7F) or 0x80)
            v = v ushr 7
        }
    }

    private fun ByteArrayInputStream.readVarInt(): Int {
        var result = 0; var shift = 0; var b: Int
        do { b = read(); result = result or ((b and 0x7F) shl shift); shift += 7 } while (b and 0x80 != 0)
        return result
    }

    private fun ByteArrayInputStream.readVarIntString(): String {
        val len = readVarInt()
        return String(readNBytes(len), Charsets.UTF_8)
    }
}