package computer.obscure.piku.mod.fabric.utils

import me.znotchill.kiwi.generated.Vec3

fun Vec3.toMcVec3() = net.minecraft.world.phys.Vec3(
    this.x, this.y, this.z
)
fun net.minecraft.world.phys.Vec3.toCoreVec3() = Vec3(
    this.x, this.y, this.z
)