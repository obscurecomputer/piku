package computer.obscure.piku.minestom.scripting

import io.netty.buffer.ByteBufAllocator
import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.server.BlossomServer
import computer.obscure.piku.core.scripting.api.LuaEventData
import computer.obscure.piku.core.scripting.server.ServerAPI
import computer.obscure.piku.core.utils.jsonStringToLua
import computer.obscure.piku.core.utils.toJson
import computer.obscure.piku.core.utils.writeString
import computer.obscure.piku.minestom.scripting.api.LuaPlayer
import computer.obscure.twine.nativex.conversion.Converter.toLuaValue
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerLoadedEvent
import net.minestom.server.event.player.PlayerPluginMessageEvent
import net.minestom.server.network.NetworkBuffer
import net.minestom.server.network.packet.server.common.PluginMessagePacket
import org.luaj.vm2.LuaValue

class MinestomAPI(val server: BlossomServer) : ServerAPI<Player> {
    override val engine = MinestomLuaEngine()

    override fun registerEvents() {
        engine.init()
        server.eventHandler.addListener<PlayerPluginMessageEvent> { event ->
            when (event.identifier) {
                "piku:send_data" -> {
                    try {
                        val outerBuffer = NetworkBuffer.wrap(event.message, 0, event.message.size)

                        val innerBytes = outerBuffer.read(NetworkBuffer.BYTE_ARRAY)

                        val innerBuffer = NetworkBuffer.wrap(innerBytes, 0, innerBytes.size)

                        val eventId = innerBuffer.read(NetworkBuffer.STRING)
                        val dataJson = innerBuffer.read(NetworkBuffer.STRING)

                        val luaData = jsonStringToLua(dataJson)
                        engine.events.fire(eventId, luaData, event.player)
                    } catch (e: Exception) {
                        error("Failed to decode: ${e.message}")
                    }
                }
            }
        }

        server.eventHandler.addListener<PlayerLoadedEvent> { event ->
            val player = event.player

            engine.events.fire(
                "server.player_loaded",
                LuaEventData(
                    mapOf(
                        "player" to LuaPlayer(player).table
                    )
                ).table
            )
        }
    }

    override fun sendData(player: Player, eventId: String, data: Any) {
        val serializedData: LuaValue = when (data) {
            is LuaEventData -> data.toLuaValue(data)
            else -> data.toLuaValue()
        }

        val buf = ByteBufAllocator.DEFAULT.buffer()
        buf.writeString(eventId)
        buf.writeString(serializedData.toJson())

        val bytes = ByteArray(buf.readableBytes())
        buf.readBytes(bytes)
        buf.release()

        player.sendPacket(
            PluginMessagePacket("piku:receive_data", bytes)
        )
    }

    override fun getPlayerUD(player: Player): LuaValue {
        val playerUd = LuaValue.userdataOf(LuaPlayer(player))
        return playerUd
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
}