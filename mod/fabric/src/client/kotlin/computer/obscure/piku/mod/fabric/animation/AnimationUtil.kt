package computer.obscure.piku.mod.fabric.animation

import computer.obscure.piku.core.ui.classes.Easing
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.twine.LuaCallback
import net.minecraft.world.phys.Vec3

object AnimationUtil {

    fun resolveEasing(
        easing: String,
        t: Double,
        custom: Map<String, LuaCallback>
    ): Double {
        return try {
            val eased = (custom[easing]?.call(t)
                ?: Easing.valueOf(easing.uppercase()).getValue(t))
            (eased as Double).coerceIn(0.0, 1.0)
        } catch (e: Exception) {
            e.printStackTrace()
            PikuClient.LOGGER.warn("Invalid easing \"$easing\"! Defaulting to LINEAR")
            Easing.LINEAR.getValue(t)
        }
    }

    fun lerp(start: Float, end: Float, t: Double): Float =
        start + ((end - start) * t).toFloat()

    fun lerp(start: Double, end: Double, t: Double): Double =
        start + (end - start) * t

    fun lerp(start: Vec3, end: Vec3, t: Double): Vec3 =
        start.lerp(end, t)

    fun lerp(
        start: computer.obscure.piku.core.classes.Vec3,
        end: computer.obscure.piku.core.classes.Vec3,
        t: Double
    ): computer.obscure.piku.core.classes.Vec3 =
        computer.obscure.piku.core.classes.Vec3(
            lerp(start.x, end.x, t),
            lerp(start.y, end.y, t),
            lerp(start.z, end.z, t)
        )
}
