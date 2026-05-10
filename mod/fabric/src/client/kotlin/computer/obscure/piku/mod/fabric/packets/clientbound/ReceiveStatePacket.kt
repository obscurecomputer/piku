package computer.obscure.piku.mod.fabric.packets.clientbound

import computer.obscure.piku.core.scripting.server.SharedStateManager
import computer.obscure.piku.core.utils.jsonStringToKotlin
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.packets.CustomPacket
import computer.obscure.piku.mod.fabric.scripting.api.LuaSharedState
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

class ReceiveStatePacket(
    val internalId: String,
    val name: String,
    val value: String,
    val clientModifiable: Boolean = false
) : CustomPacket {
    override val packetType get() = TYPE

    override fun handle() {
        Minecraft.getInstance().execute {
            try {
                val value = jsonStringToKotlin(value)
                val luaState = LuaSharedState(
                    internalId = internalId,
                    name = name,
                    value = value,
                    clientModifiable = clientModifiable
                )

                val state = luaState.toSharedState()

                PikuClient.engine!!.events.stateCallbacks[state.internalId]?.invoke(
                    mapOf("value" to value)
                )

                SharedStateManager.addState(state)
                PikuClient.engine!!.events.fire(
                    "client.update_state",
                    mapOf(
                        "internalId" to luaState.internalId,
                        "name" to luaState.name,
                        "value" to luaState.value,
                        "clientModifiable" to luaState.clientModifiable
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        val TYPE = CustomPacketPayload.Type<ReceiveStatePacket>(
            Identifier.fromNamespaceAndPath("piku", "receive_state")
        )
        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, ReceiveStatePacket> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ReceiveStatePacket::internalId,
            ByteBufCodecs.STRING_UTF8, ReceiveStatePacket::name,
            ByteBufCodecs.STRING_UTF8, ReceiveStatePacket::value,
            ByteBufCodecs.BOOL, ReceiveStatePacket::clientModifiable,
            ::ReceiveStatePacket
        )
    }
}