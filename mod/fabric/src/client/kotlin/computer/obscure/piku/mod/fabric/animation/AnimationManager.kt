package computer.obscure.piku.mod.fabric.animation

import computer.obscure.piku.core.classes.Vec3
import computer.obscure.piku.core.service.PikuService
import computer.obscure.piku.mod.fabric.ui.UIRenderer

object AnimationManager : PikuService {
    private val animations: MutableList<Animation<*>> = mutableListOf()
    private val pending: MutableList<Animation<*>> = mutableListOf()

    override fun shutdown() {
        animations.clear()
        pending.clear()
    }

    fun <T> animate(anim: Animation<T>) {
        if (anim.from == null) anim.from = anim.getter()
        pending += anim
    }

    fun tick(deltaSeconds: Double) {
        animations += pending
        pending.clear()
        val it = animations.iterator()
        while (it.hasNext()) {
            val anim = it.next()
            if (!anim.started) {
                anim.started = true
                anim.onStart()
            }
            anim.elapsed = (anim.elapsed + deltaSeconds).coerceAtMost(anim.durationSeconds)

            val t = (anim.elapsed / anim.durationSeconds)

            val eased = AnimationUtil.resolveEasing(
                anim.easing,
                t,
                UIRenderer.registeredEasings
            )

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
            anim.onTick(deltaSeconds, t)

            if (anim.elapsed >= anim.durationSeconds) {
                anim.finished = true
                anim.onFinish()
                it.remove()
            }
        }
    }
}