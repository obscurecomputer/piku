package computer.obscure.piku.mod.fabric.packets

import net.minecraft.network.protocol.common.custom.CustomPacketPayload

interface CustomPacket : CustomPacketPayload {
    val packetType: CustomPacketPayload.Type<out CustomPacketPayload>
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = packetType

    fun handle() {}
}