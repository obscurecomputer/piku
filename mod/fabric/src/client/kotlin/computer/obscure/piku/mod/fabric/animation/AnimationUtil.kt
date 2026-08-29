package computer.obscure.piku.mod.fabric.animation

import computer.obscure.piku.core.classes.Easing
import computer.obscure.piku.core.classes.Vec3
import computer.obscure.twine.LuaCallback
import me.znotchill.kiwi.generated.Vec2
import net.minecraft.world.phys.Vec3 as McVec3

object AnimationUtil {

    fun resolveEasing(
        easing: String,
        t: Double,
        custom: Map<String, LuaCallback>
    ): Double {
        return try {
            val eased = (custom[easing]?.call<Double>(t)
                ?: Easing.valueOf(easing.uppercase()).getValue(t))
            eased.coerceIn(0.0, 1.0)
        } catch (e: Exception) {
            e.printStackTrace()
            Easing.LINEAR.getValue(t)
        }
    }

    fun lerp(start: Float, end: Float, t: Double): Float =
        start + ((end - start) * t).toFloat()

    fun lerp(start: Double, end: Double, t: Double): Double =
        start + (end - start) * t

    fun lerp(
        start: Vec3,
        end: Vec3,
        t: Double
    ): Vec3 =
        Vec3(
            lerp(start.x, end.x, t),
            lerp(start.y, end.y, t),
            lerp(start.z, end.z, t)
        )

    fun lerp(start: Vec2, end: Vec2, t: Double): Vec2 = Vec2(
        lerp(start.x, end.x, t),
        lerp(start.y, end.y, t)
    )
}


fun AnimationUtil.lerp(start: McVec3, end: McVec3, t: Double): McVec3 =
    start.lerp(end, t)