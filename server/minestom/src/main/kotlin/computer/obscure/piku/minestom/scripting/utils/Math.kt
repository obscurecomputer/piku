package computer.obscure.piku.minestom.scripting.utils

import computer.obscure.piku.core.classes.BlockVec as CoreBlockVec
import net.minestom.server.coordinate.BlockVec

fun BlockVec.toCore() = CoreBlockVec(
    this.blockX, this.blockY, this.blockZ
)