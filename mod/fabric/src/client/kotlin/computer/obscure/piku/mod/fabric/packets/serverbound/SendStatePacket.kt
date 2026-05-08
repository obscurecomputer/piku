package computer.obscure.piku.mod.fabric.packets.serverbound

import computer.obscure.piku.mod.fabric.packets.CustomPacket
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

class SendStatePacket(
    val internalId: String,
    val value: String
) : CustomPacket {
    override val packetType get() = TYPE

    companion object {
        val TYPE = CustomPacketPayload.Type<SendStatePacket>(
            Identifier.fromNamespaceAndPath("piku", "send_state")
        )
        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, SendStatePacket> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SendStatePacket::internalId,
            ByteBufCodecs.STRING_UTF8, SendStatePacket::value,
            ::SendStatePacket
        )
    }
}
