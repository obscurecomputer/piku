package computer.obscure.piku.mod.fabric.packets.serverbound

import computer.obscure.piku.mod.fabric.packets.CustomPacket
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

class SendUnloadedPacket(
    val reloadId: Long
) : CustomPacket {
    override val packetType get() = TYPE

    companion object {
        val TYPE = CustomPacketPayload.Type<SendUnloadedPacket>(
            Identifier.fromNamespaceAndPath("piku", "finished_unloading_scripts")
        )
        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, SendUnloadedPacket> = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, SendUnloadedPacket::reloadId,
            ::SendUnloadedPacket
        )
    }
}