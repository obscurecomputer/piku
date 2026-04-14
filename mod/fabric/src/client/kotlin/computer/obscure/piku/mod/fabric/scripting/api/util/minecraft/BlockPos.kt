package computer.obscure.piku.mod.fabric.scripting.api.util.minecraft

import net.minecraft.core.BlockPos

fun BlockPos.toCoreVec3(): computer.obscure.piku.core.classes.Vec3 {
    return computer.obscure.piku.core.classes.Vec3(
        this.x, this.y, this.z
    )
}