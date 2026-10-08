package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineProperty
import net.minecraft.world.level.block.Block

class LuaBlock(
    val block: Block
) : TwineNative() {
    @TwineProperty
    val friction: Float
        get() = block.friction
    @TwineProperty
    val speed: Float
        get() = block.speedFactor
    @TwineProperty
    val jump: Float
        get() = block.jumpFactor
    @TwineProperty
    val descriptionId: String
        get() = block.descriptionId
}