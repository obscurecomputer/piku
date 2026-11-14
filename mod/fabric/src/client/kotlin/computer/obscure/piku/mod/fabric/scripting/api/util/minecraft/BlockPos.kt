package computer.obscure.piku.mod.fabric.scripting.api.util.minecraft

import me.znotchill.kiwi.generated.Vec3
import net.minecraft.core.BlockPos

fun BlockPos.toCoreVec3(): Vec3 {
    return Vec3(
        this.x, this.y, this.z
    )
}