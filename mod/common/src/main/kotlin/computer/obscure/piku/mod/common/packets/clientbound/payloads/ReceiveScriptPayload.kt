package computer.obscure.piku.mod.common.packets.clientbound.payloads

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation

class ReceiveScriptPayload(val name: String, val fileContents: String) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<ReceiveScriptPayload> = TYPE

    companion object {
        val ID: ResourceLocation = ResourceLocation.fromNamespaceAndPath("piku", "receive_script")
        val TYPE = CustomPacketPayload.Type<ReceiveScriptPayload>(
            ID
        )

        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, ReceiveScriptPayload> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ReceiveScriptPayload::name,
            ByteBufCodecs.STRING_UTF8, ReceiveScriptPayload::fileContents,
            ::ReceiveScriptPayload
        )
    }
}