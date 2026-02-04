package computer.obscure.piku.fabric

import computer.obscure.piku.mod.common.Piku
import net.fabricmc.api.ClientModInitializer

class PikuClient : ClientModInitializer {
    override fun onInitializeClient() {
        Piku.init()
    }
}
