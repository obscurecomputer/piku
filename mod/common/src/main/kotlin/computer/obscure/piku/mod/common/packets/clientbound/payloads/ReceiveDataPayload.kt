package computer.obscure.piku.mod.common.packets.clientbound.payloads

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation

class ReceiveDataPayload(val id: String, val json: String) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<ReceiveDataPayload> = TYPE

    companion object {
        val TYPE = CustomPacketPayload.Type<ReceiveDataPayload>(
            ResourceLocation.fromNamespaceAndPath("piku", "receive_data")
        )

        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, ReceiveDataPayload> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ReceiveDataPayload::id,
            ByteBufCodecs.stringUtf8(1048576), ReceiveDataPayload::json,
            ::ReceiveDataPayload
        )
    }
}
