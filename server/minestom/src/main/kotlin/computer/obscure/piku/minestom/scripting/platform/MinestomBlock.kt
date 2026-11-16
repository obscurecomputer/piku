package computer.obscure.piku.minestom.scripting.platform

import computer.obscure.piku.core.scripting.platform.CoreBlock
import computer.obscure.twine.TwineNative
import net.kyori.adventure.key.Key
import net.minestom.server.instance.block.Block

class MinestomBlock(
    val block: Block
) : CoreBlock, TwineNative() {
    override val properties: Map<String, String>
        get() = block.properties()

    override val key: Key
        get() = block.key()
}