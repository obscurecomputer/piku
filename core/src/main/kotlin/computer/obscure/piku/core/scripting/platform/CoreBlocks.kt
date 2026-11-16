package computer.obscure.piku.core.scripting.platform

import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import net.kyori.adventure.key.Key

class CoreBlocks : TwineNative("blocks") {
    @TwineFunction
    fun get(string: String): CoreBlock? {
        return try {
            CoreBlockDto(
                key = Key.key(string)
            ).fromDto()
        } catch (_: Exception) {
            null
        }
    }
}