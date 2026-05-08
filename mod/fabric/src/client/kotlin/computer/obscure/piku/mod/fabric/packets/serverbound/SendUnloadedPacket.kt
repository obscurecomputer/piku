package computer.obscure.piku.mod.fabric.packets.serverbound

import computer.obscure.piku.mod.fabric.packets.CustomPacket
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

class SendUnloadedPacket() : CustomPacket {
    override val packetType get() = TYPE

    companion object {
        val TYPE = CustomPacketPayload.Type<SendUnloadedPacket>(
            Identifier.fromNamespaceAndPath("piku", "finished_unloading_scripts")
        )
        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, SendUnloadedPacket> = StreamCodec.of(
            { _, _ -> },
            { SendUnloadedPacket() }
        )
    }
}
