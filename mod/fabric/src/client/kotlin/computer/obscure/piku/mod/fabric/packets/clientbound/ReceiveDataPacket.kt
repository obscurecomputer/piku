package computer.obscure.piku.mod.fabric.packets.clientbound

import computer.obscure.piku.core.utils.jsonStringToKotlin
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.packets.CustomPacket
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

class ReceiveDataPacket(
    val id: String,
    val json: String
) : CustomPacket {
    override val packetType get() = TYPE

    override fun handle() {
        Minecraft.getInstance().execute {
            try {
                val engine = PikuClient.engine
                    ?: return@execute

                val data = jsonStringToKotlin(json)
                println("receive_data $id: $json")

                engine.serverEvents.receive(id, data, Unit)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        val TYPE = CustomPacketPayload.Type<ReceiveDataPacket>(
            Identifier.fromNamespaceAndPath("piku", "receive_data")
        )
        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, ReceiveDataPacket> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ReceiveDataPacket::id,
            ByteBufCodecs.STRING_UTF8, ReceiveDataPacket::json,
            ::ReceiveDataPacket
        )
    }
}