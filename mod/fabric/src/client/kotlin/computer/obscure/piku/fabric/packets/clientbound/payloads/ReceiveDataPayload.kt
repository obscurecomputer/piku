package computer.obscure.piku.fabric.packets.clientbound.payloads

import computer.obscure.piku.fabric.packets.clientbound.ClientPackets
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload

class ReceiveDataPayload(
    val id: String,
    val json: String
) : CustomPayload {

    companion object {
        val ID = CustomPayload.Id<ReceiveDataPayload>(ClientPackets.RECEIVE_DATA)

        val CODEC: PacketCodec<PacketByteBuf, ReceiveDataPayload> =
            PacketCodec.of(
                { payload, buf ->
                    buf.writeString(payload.id)
                    buf.writeString(payload.json)
                },
                { buf ->
                    ReceiveDataPayload(
                        buf.readString(),
                        buf.readString()
                    )
                }
            )
    }

    override fun getId() = ID
}