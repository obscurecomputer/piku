package computer.obscure.piku.mod.common.packets.clientbound.payloads

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import java.util.UUID

class ReceiveStatePayload(
    val internalId: String,
    val name: String,
    val value: String,
    val clientModifiable: Boolean = false
) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<ReceiveStatePayload> = TYPE

    companion object {
        val ID: Identifier = Identifier.fromNamespaceAndPath("piku", "receive_state")
        val TYPE = CustomPacketPayload.Type<ReceiveStatePayload>(
            ID
        )

        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, ReceiveStatePayload> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ReceiveStatePayload::internalId,
            ByteBufCodecs.STRING_UTF8, ReceiveStatePayload::name,
            ByteBufCodecs.STRING_UTF8, ReceiveStatePayload::value,
            ByteBufCodecs.BOOL, ReceiveStatePayload::clientModifiable,
            ::ReceiveStatePayload
        )
    }
}