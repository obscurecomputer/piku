package computer.obscure.piku.minestom.scripting

import computer.obscure.piku.core.scripting.server.ServerAPI
import computer.obscure.piku.core.scripting.server.SharedStateManager
import computer.obscure.piku.core.states.SharedState
import computer.obscure.piku.core.utils.jsonStringToKotlin
import computer.obscure.piku.core.utils.readString
import computer.obscure.piku.core.utils.toJson
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.server.BlossomServer
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerPluginMessageEvent
import net.minestom.server.network.NetworkBuffer
import net.minestom.server.network.packet.server.common.PluginMessagePacket
import java.util.*

class MinestomAPI(val server: BlossomServer) : ServerAPI<Player> {
    override val engine = MinestomLuaEngine()

    override fun registerEvents() {
        SharedStateManager.piku = this
        engine.init()
        server.eventHandler.addListener<PlayerPluginMessageEvent> { event ->
            when (event.identifier) {
                "piku:send_data" -> {
                    try {
                        val buffer: ByteBuf = Unpooled.wrappedBuffer(event.message)

                        val eventId = buffer.readString()
                        val data = buffer.readString()

                        buffer.release()

                        val decoded = jsonStringToKotlin(data)
                        @Suppress("UNCHECKED_CAST")
                        val luaData = (decoded as? Map<String, Any?>) ?: emptyMap()

                        engine.events.fire(eventId, luaData, event.player)
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
                            jsonStringToKotlin(value)!!,
                            exemptSyncOwners = listOf(event.player)
                        )
                    } catch (e: Exception) {
                        error("Failed to decode: ${e.message}")
                    }
                }
            }
        }
    }

    override fun sendData(player: Player, eventId: String, data: Any) {
        val jsonString = data.toJson()

        val bytes = NetworkBuffer.makeArray { buffer ->
            buffer.write(NetworkBuffer.STRING, eventId)
            buffer.write(NetworkBuffer.STRING, jsonString)
        }

        player.sendPacket(PluginMessagePacket("piku:receive_data", bytes))
    }

    override fun sendState(player: Player, state: SharedState) {
        val serializedData = state.value.toJson()

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
}