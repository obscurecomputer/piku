package computer.obscure.piku.minestom.scripting.api.instance

import computer.obscure.piku.core.classes.BlockVec
import computer.obscure.piku.minestom.scripting.platform.MinestomBlock
import computer.obscure.piku.minestom.scripting.utils.toCore
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import net.minestom.server.instance.generator.GenerationUnit

class LuaInstanceUnit(val unit: GenerationUnit) : TwineNative() {
    @TwineProperty val absoluteStart = unit.absoluteStart().toCore()
    @TwineProperty val absoluteEnd = unit.absoluteEnd().toCore()
    @TwineProperty val size = unit.size().toCore()

    @TwineFunction
    fun set(blockVec: BlockVec, block: MinestomBlock) {
        unit.modifier().setBlock(
            blockVec.x, blockVec.y, blockVec.z, block.block
        )
    }
}