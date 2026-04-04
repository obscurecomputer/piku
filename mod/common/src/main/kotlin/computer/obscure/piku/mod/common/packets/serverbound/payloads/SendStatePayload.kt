package computer.obscure.piku.mod.common.packets.serverbound.payloads

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

class SendStatePayload(
    val internalId: String,
    val value: String
) : CustomPacketPayload {

    companion object {
        val ID = Identifier.fromNamespaceAndPath("piku", "send_state")
        val TYPE = CustomPacketPayload.Type<SendStatePayload>(ID)

        val CODEC: StreamCodec<FriendlyByteBuf, SendStatePayload> = StreamCodec.of(
            { buf, payload ->
                buf.writeUtf(payload.internalId)
                buf.writeUtf(payload.value)
            },
            { buf ->
                SendStatePayload(
                    buf.readUtf(),
                    buf.readUtf()
                )
            }
        )
    }

    override fun type(): CustomPacketPayload.Type<SendStatePayload> = TYPE
}