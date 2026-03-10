package computer.obscure.piku.mod.common.packets.serverbound.payloads

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation

class SendDataPayload(
    val id: String,
    val json: String
) : CustomPacketPayload {

    companion object {
        val ID = ResourceLocation.fromNamespaceAndPath("piku", "send_data")
        val TYPE = CustomPacketPayload.Type<SendDataPayload>(ID)

        val CODEC: StreamCodec<FriendlyByteBuf, SendDataPayload> = StreamCodec.of(
            { buf, payload ->
                buf.writeUtf(payload.id)
                buf.writeUtf(payload.json)
            },
            { buf ->
                SendDataPayload(
                    buf.readUtf(),
                    buf.readUtf()
                )
            }
        )
    }

    override fun type(): CustomPacketPayload.Type<SendDataPayload> = TYPE
}