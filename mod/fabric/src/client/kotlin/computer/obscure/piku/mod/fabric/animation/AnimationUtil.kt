package computer.obscure.piku.mod.fabric.animation

import computer.obscure.piku.core.animation.AnimationUtil
import net.minecraft.world.phys.Vec3

fun AnimationUtil.lerp(start: Vec3, end: Vec3, t: Double): Vec3 =
    start.lerp(end, t)