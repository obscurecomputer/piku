package me.znotchill.piku.client.packets.clientbound.payloads

import me.znotchill.piku.client.packets.clientbound.ClientPackets
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload

class ReceiveScriptPayload(
    val name: String,
    val fileContents: String
) : CustomPayload {

    companion object {
        val ID = CustomPayload.Id<ReceiveScriptPayload>(ClientPackets.RECEIVE_SCRIPT)

        val CODEC: PacketCodec<PacketByteBuf, ReceiveScriptPayload> = PacketCodec.tuple(
            PacketCodecs.STRING, ReceiveScriptPayload::name,
            PacketCodecs.STRING, ReceiveScriptPayload::fileContents,
            ::ReceiveScriptPayload
        )
    }

    override fun getId() = ID
}