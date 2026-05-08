package computer.obscure.piku.mod.fabric.packets.clientbound

import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.packets.CustomPacket
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

class ReceiveScriptPacket(
    val name: String,
    val fileContents: String
) : CustomPacket {
    override val packetType get() = TYPE

    override fun handle() {
        val engineRef = PikuClient.engine
        Minecraft.getInstance().execute {
            try {
                if (PikuClient.engine !== engineRef) {
                    PikuClient.LOGGER.error("Cannot load script ${name}: Engine reference lost!")
                    return@execute
                }
                if (name == "END_OF_SCRIPT_LOADING") {
                    PikuClient.LOGGER.debug("Server sent EOSL")
                    engineRef!!.activeScripts.forEach { script ->
                        engineRef.runScript(script.key, script.value)
                    }
                    return@execute
                }
                PikuClient.LOGGER.debug("Loading script ${name}: ${fileContents.length} bytes")
                PikuClient.engine!!.activeScripts[name] = fileContents
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        val TYPE = CustomPacketPayload.Type<ReceiveScriptPacket>(
            Identifier.fromNamespaceAndPath("piku", "receive_script")
        )
        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, ReceiveScriptPacket> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ReceiveScriptPacket::name,
            ByteBufCodecs.STRING_UTF8, ReceiveScriptPacket::fileContents,
            ::ReceiveScriptPacket
        )
    }
}