package computer.obscure.piku.mod.fabric.packets.serverbound.payloads

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

class SendUnloadedPayload : CustomPacketPayload {

    companion object {
        val ID = Identifier.fromNamespaceAndPath("piku", "finished_unloading_scripts")
        val TYPE = CustomPacketPayload.Type<SendUnloadedPayload>(ID)

        val CODEC: StreamCodec<FriendlyByteBuf, SendUnloadedPayload> = StreamCodec.of(
            { _, _ -> },
            { SendUnloadedPayload() }
        )
    }

    override fun type(): CustomPacketPayload.Type<SendUnloadedPayload> = TYPE
}