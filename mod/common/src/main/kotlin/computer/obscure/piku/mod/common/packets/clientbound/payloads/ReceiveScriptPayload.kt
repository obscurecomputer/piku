package computer.obscure.piku.mod.common.packets.clientbound.payloads

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation

class ReceiveScriptPayload(val name: String, val fileContents: String) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<ReceiveScriptPayload> = TYPE

    companion object {
        val TYPE = CustomPacketPayload.Type<ReceiveScriptPayload>(
            ResourceLocation.fromNamespaceAndPath("piku", "receive_script")
        )

        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, ReceiveScriptPayload> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ReceiveScriptPayload::name,
            ByteBufCodecs.stringUtf8(1048576), ReceiveScriptPayload::fileContents,
            ::ReceiveScriptPayload
        )
    }
}