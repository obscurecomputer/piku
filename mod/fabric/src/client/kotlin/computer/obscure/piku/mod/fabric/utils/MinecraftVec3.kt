package computer.obscure.piku.mod.fabric.utils

import net.minecraft.world.phys.Vec3

fun Vec3.toCoreVec3(): computer.obscure.piku.core.classes.Vec3 {
    return computer.obscure.piku.core.classes.Vec3(
        this.x,
        this.y,
        this.z
    )
}