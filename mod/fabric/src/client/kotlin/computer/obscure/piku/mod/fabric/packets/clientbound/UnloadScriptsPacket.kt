package computer.obscure.piku.mod.fabric.packets.clientbound

import computer.obscure.piku.mod.fabric.Client
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.events.ClientPlayConnection
import computer.obscure.piku.mod.fabric.packets.CustomPacket
import computer.obscure.piku.mod.fabric.packets.serverbound.SendUnloadedPacket
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

class UnloadScriptsPacket(
    val reloadId: Long
) : CustomPacket {
    override val packetType get() = TYPE

    override fun handle() {
        Minecraft.getInstance().execute {
            try {
                ClientPlayConnection.onDisconnect(true) {
                    Client.connectedToServer = true
                    PikuClient.engine!!.init()
                    ClientPlayNetworking.send(SendUnloadedPacket(reloadId))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        val TYPE = CustomPacketPayload.Type<UnloadScriptsPacket>(
            Identifier.fromNamespaceAndPath("piku", "unload_scripts")
        )
        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, UnloadScriptsPacket> = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, UnloadScriptsPacket::reloadId,
            ::UnloadScriptsPacket
        )
    }
}