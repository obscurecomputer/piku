package computer.obscure.piku.mod.fabric.sound

import net.minecraft.resources.Identifier

data class LayerDef(
    val id: String,
    val sound: Identifier,
    val defaultVolume: Float = 0f,
)
