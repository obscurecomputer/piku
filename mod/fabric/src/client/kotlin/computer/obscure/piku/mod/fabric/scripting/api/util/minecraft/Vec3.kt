package computer.obscure.piku.mod.fabric.scripting.api.util.minecraft

import me.znotchill.kiwi.generated.Vec3
import net.minecraft.world.phys.Vec3 as MCVec3

fun MCVec3.toCoreVec3(): Vec3 {
    return Vec3(
        this.x, this.y, this.z
    )
}

fun MCVec3.toLua(): Vec3 {
    return Vec3(
        this.x, this.y, this.z
    )
}

fun Vec3.toMCVec3(): Vec3 {
    return Vec3(
        this.x, this.y, this.z
    )
}