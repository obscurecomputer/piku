package computer.obscure.piku.fabric.animation

import computer.obscure.piku.common.ui.classes.Easing
import net.minecraft.util.math.Vec3d

object AnimationUtil {

    fun resolveEasing(
        easing: String,
        t: Double,
        custom: Map<String, (Double) -> Double>
    ): Double {
        return try {
            (custom[easing]?.invoke(t)
                ?: Easing.valueOf(easing.uppercase()).getValue(t))
                .coerceIn(0.0, 1.0)
        } catch (_: Exception) {
            Easing.LINEAR.getValue(t)
        }
    }

    fun lerp(start: Float, end: Float, t: Double): Float =
        start + ((end - start) * t).toFloat()

    fun lerp(start: Double, end: Double, t: Double): Double =
        start + (end - start) * t

    fun lerp(start: Vec3d, end: Vec3d, t: Double): Vec3d =
        start.lerp(end, t)
}
