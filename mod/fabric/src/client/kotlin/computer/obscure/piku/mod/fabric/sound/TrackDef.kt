package computer.obscure.piku.mod.fabric.sound

import net.minecraft.sounds.SoundSource

data class TrackDef(
    val id: String,
    val layers: List<LayerDef>,
    val loop: Boolean = true,
    val category: SoundSource = SoundSource.MUSIC
)