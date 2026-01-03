package computer.obscure.piku.client.packets.clientbound

import net.minecraft.util.Identifier

object ClientPackets {
    val RECEIVE_SCRIPT: Identifier = Identifier.of("piku", "receive_script")
    val RECEIVE_DATA: Identifier = Identifier.of("piku", "receive_data")
}