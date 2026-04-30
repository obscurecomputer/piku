package computer.obscure.piku.mod.fabric.packets.clientbound.payloads

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

class UnloadScriptsPayload(
    val blank: Boolean = true
) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<UnloadScriptsPayload> = TYPE

    companion object {
        val ID: Identifier = Identifier.fromNamespaceAndPath("piku", "unload_scripts")
        val TYPE = CustomPacketPayload.Type<UnloadScriptsPayload>(
            ID
        )

        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, UnloadScriptsPayload> = StreamCodec.composite(
            ByteBufCodecs.BOOL, UnloadScriptsPayload::blank,
            ::UnloadScriptsPayload
        )
    }
}