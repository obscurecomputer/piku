package computer.obscure.piku.core.scripting.api

import computer.obscure.piku.core.classes.BlockVec
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction

class BlockVecBuilder : TwineNative("blockvec") {
    @TwineFunction
    fun new(x: Number, y: Number, z: Number) = BlockVec(
        x.toInt(), y.toInt(), z.toInt()
    )
}