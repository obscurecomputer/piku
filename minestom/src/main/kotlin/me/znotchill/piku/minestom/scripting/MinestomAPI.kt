package me.znotchill.piku.minestom.scripting

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.Unpooled
import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.server.BlossomServer
import me.znotchill.piku.common.scripting.server.ServerAPI
import me.znotchill.piku.common.utils.jsonStringToLua
import me.znotchill.piku.common.utils.jsonToLua
import me.znotchill.piku.common.utils.readString
import me.znotchill.piku.common.utils.writeString
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerPluginMessageEvent
import net.minestom.server.network.packet.server.common.PluginMessagePacket

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

                    engine.events.fire(eventId, luaData)
                }
            }
        }
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