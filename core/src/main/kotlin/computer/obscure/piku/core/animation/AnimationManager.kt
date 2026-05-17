package computer.obscure.piku.core.animation

import computer.obscure.piku.core.classes.Spacing
import computer.obscure.piku.core.classes.Vec2
import computer.obscure.piku.core.classes.Vec3
import computer.obscure.piku.core.service.PikuService

object AnimationManager : PikuService {
    private val animations: MutableList<Animation<Any>> = mutableListOf()
    private val pending: MutableList<Animation<Any>> = mutableListOf()

    var easingResolver: (String, Double) -> Double = { _, t -> t }

    override fun shutdown() {
        animations.clear()
        pending.clear()
    }

    fun animations(): List<Animation<*>>
        = animations.toList()

    fun isAnimating(id: String): Boolean {
        return animations.any { it.targetId == id } || pending.any { it.targetId == id }
    }

    fun cancelFor(id: String) {
        animations.removeAll { it.targetId == id && !it.finished }
        pending.removeAll { it.targetId == id }
    }

    fun <T> animate(anim: Animation<T>) {
        @Suppress("UNCHECKED_CAST")
        pending += anim as Animation<Any>
    }

    fun tick(deltaSeconds: Double) {
        animations += pending
        pending.clear()

        val toRemove = mutableListOf<Animation<*>>()

        for (anim in animations) {
            if (!anim.started) {
                anim.started = true
                anim.onStart()
                if (anim.from == null)
                    anim.from = anim.getter()
            }
            anim.elapsed = (anim.elapsed + deltaSeconds).coerceAtMost(anim.durationSeconds)

            val t = anim.elapsed / anim.durationSeconds
            val eased = easingResolver(anim.easing, t)
            val from = anim.from
            val to = anim.to

            val value = when (from) {
                is Float -> AnimationUtil.lerp(from, to as Float, eased)
                is Double -> AnimationUtil.lerp(from, to as Double, eased)
                is Int -> AnimationUtil.lerp(from.toFloat(), (to as Int).toFloat(), eased).toInt()
                is Vec3 -> AnimationUtil.lerp(from, to as Vec3, eased)
                is Vec2 -> Vec2(
                    AnimationUtil.lerp(from.x, (to as Vec2).x, eased),
                    AnimationUtil.lerp(from.y, to.y, eased)
                )
                is Spacing -> {
                    val t2 = to as Spacing
                    Spacing(
                        left = AnimationUtil.lerp(from.left, t2.left, eased),
                        top = AnimationUtil.lerp(from.top, t2.top, eased),
                        right = AnimationUtil.lerp(from.right, t2.right, eased),
                        bottom = AnimationUtil.lerp(from.bottom, t2.bottom, eased)
                    )
                }
                else -> to
            }

            @Suppress("UNCHECKED_CAST")
            anim.setter(value)
            anim.onTick(deltaSeconds, t)

            if (anim.elapsed >= anim.durationSeconds) {
                anim.finished = true
                toRemove += anim
            }
        }

        // remove finished animations, then fire onFinish
        // so any new animations queued by onFinish go to pending safely
        animations.removeAll(toRemove)
        toRemove.forEach { it.onFinish() }
    }
}