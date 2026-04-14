package computer.obscure.piku.mod.fabric.scripting.api.util.minecraft

import computer.obscure.piku.core.scripting.api.LuaVec3Instance
import net.minecraft.world.phys.Vec3

fun Vec3.toCoreVec3(): computer.obscure.piku.core.classes.Vec3 {
    return computer.obscure.piku.core.classes.Vec3(
        this.x, this.y, this.z
    )
}

fun LuaVec3Instance.toMCVec3(): Vec3 {
    return Vec3(
        this.x, this.y, this.z
    )
}