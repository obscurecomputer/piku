package computer.obscure.piku.mod.common.camera

import computer.obscure.piku.mod.common.animation.AnimationUtil
import computer.obscure.piku.core.ui.events.ValueAnimation
import computer.obscure.piku.mod.common.ui.UIRenderer
import net.minecraft.world.phys.Vec3

object CameraAnimator {

    private val animations = mutableListOf<ValueAnimation<*>>()

    fun <T> animate(anim: ValueAnimation<T>) {
        if (anim.from == null) anim.from = anim.getter()
        animations += anim
    }

    fun tick(deltaSeconds: Double) {
        CinematicCamera.prevPos = CinematicCamera.pos
        CinematicCamera.prevYaw = CinematicCamera.yaw
        CinematicCamera.prevPitch = CinematicCamera.pitch

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
                is Vec3 -> AnimationUtil.lerp(from, to as Vec3, eased)
                else -> to
            }

            @Suppress("UNCHECKED_CAST")
            (anim.setter as (Any) -> Unit)(value)

            if (t >= 1.0) it.remove()
        }
    }
}
