package computer.obscure.piku.client.camera

import computer.obscure.piku.client.animation.AnimationUtil
import computer.obscure.piku.client.ui.UIRenderer
import computer.obscure.piku.common.ui.events.ValueAnimation
import net.minecraft.util.math.Vec3d

object CameraAnimator {

    private val animations = mutableListOf<ValueAnimation<*>>()

    fun <T> animate(anim: ValueAnimation<T>) {
        if (anim.from == null) anim.from = anim.getter()
        animations += anim
    }

    fun tick(deltaSeconds: Double) {
        val it = animations.iterator()
        while (it.hasNext()) {
            val anim = it.next()
            anim.elapsed += deltaSeconds

            val t = (anim.elapsed / anim.durationSeconds)
                .coerceIn(0.0, 1.0)

            val eased = AnimationUtil.resolveEasing(
                anim.easing,
                t,
                // TODO: move this, this is horrible design
                UIRenderer.registeredEasings
            )

            @Suppress("UNCHECKED_CAST")
            val from = anim.from as Any
            val to = anim.to as Any

            val value = when (from) {
                is Float -> AnimationUtil.lerp(from, to as Float, eased)
                is Double -> AnimationUtil.lerp(from, to as Double, eased)
                is Vec3d -> AnimationUtil.lerp(from, to as Vec3d, eased)
                else -> to
            }

            @Suppress("UNCHECKED_CAST")
            (anim.setter as (Any) -> Unit)(value)

            if (t >= 1.0) it.remove()
        }
    }
}
