package computer.obscure.piku.fabric.packets.serverbound.payloads

import computer.obscure.piku.fabric.packets.serverbound.ServerPackets
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload

class SendDataPayload(
    val id: String,
    val json: String
) : CustomPayload {

    companion object {
        val ID = CustomPayload.Id<SendDataPayload>(ServerPackets.SEND_DATA)

        val CODEC: PacketCodec<PacketByteBuf, SendDataPayload> =
            PacketCodec.of(
                { payload, buf ->
                    buf.writeString(payload.id)
                    buf.writeString(payload.json)
                },
                { buf ->
                    SendDataPayload(
                        buf.readString(),
                        buf.readString()
                    )
                }
            )
    }

    override fun getId() = ID
}