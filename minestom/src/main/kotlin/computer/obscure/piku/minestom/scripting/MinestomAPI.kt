package computer.obscure.piku.minestom.scripting

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.Unpooled
import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.server.BlossomServer
import computer.obscure.piku.common.scripting.api.LuaEventData
import computer.obscure.piku.common.scripting.server.ServerAPI
import computer.obscure.piku.common.utils.jsonStringToLua
import computer.obscure.piku.common.utils.readString
import computer.obscure.piku.common.utils.toJson
import computer.obscure.piku.common.utils.writeString
import computer.obscure.piku.minestom.scripting.api.LuaPlayer
import computer.obscure.twine.nativex.conversion.Converter.toLuaValue
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerLoadedEvent
import net.minestom.server.event.player.PlayerPluginMessageEvent
import net.minestom.server.network.packet.server.common.PluginMessagePacket
import org.luaj.vm2.LuaValue

class MinestomAPI(val server: BlossomServer) : ServerAPI<Player> {
    override val engine = MinestomLuaEngine()

    override fun registerEvents() {
        engine.init()
        server.eventHandler.addListener<PlayerPluginMessageEvent> { event ->
            when (event.identifier) {
                "piku:send_data" -> {
                    val buffer: ByteBuf = Unpooled.wrappedBuffer(event.message)

                    val eventId = buffer.readString()
                    val data = buffer.readString()

                    buffer.release()

                    val luaData = jsonStringToLua(data)

                    engine.events.fire(eventId, luaData, event.player)
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
            is LuaEventData -> data.table
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
        val buf = ByteBufAllocator.DEFAULT.buffer()
        buf.writeString(name)
        buf.writeString(content)

        val bytes = ByteArray(buf.readableBytes())
        buf.readBytes(bytes)
        buf.release()

        player.sendPacket(
            PluginMessagePacket("piku:receive_script", bytes)
        )
    }
}