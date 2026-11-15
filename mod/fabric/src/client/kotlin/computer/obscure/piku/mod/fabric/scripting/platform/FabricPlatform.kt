package computer.obscure.piku.mod.fabric.scripting.platform

import computer.obscure.piku.core.scripting.platform.CoreEntity
import computer.obscure.piku.core.scripting.platform.CorePlayer
import computer.obscure.piku.core.scripting.platform.Platform
import net.minecraft.client.Minecraft
import java.util.UUID

class FabricPlatform : Platform() {
    init {
        register(FabricPlayer)
        register(FabricEntity)
    }
}