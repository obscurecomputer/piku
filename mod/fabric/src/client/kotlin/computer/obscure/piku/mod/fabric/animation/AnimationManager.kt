package computer.obscure.piku.mod.fabric.animation

import computer.obscure.piku.core.classes.Spacing
import me.znotchill.kiwi.generated.Vec2
import computer.obscure.piku.core.classes.Vec3
import computer.obscure.piku.core.service.PikuService
import computer.obscure.piku.mod.fabric.ui.classes.ScaleDimension
import computer.obscure.piku.mod.fabric.ui.classes.ScaleDimension2
import me.znotchill.kiwi.generated.Color

object AnimationManager : PikuService {
    private val animations: MutableList<Animation<Any>> = mutableListOf()
    private val pending: MutableList<Animation<Any>> = mutableListOf()
    private val lock = Any()

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

    fun cancelFor(id: String) = synchronized(lock) {
        animations.removeAll { it.targetId == id }
        pending.removeAll { it.targetId == id }
    }

    fun <T> animate(anim: Animation<T>) = synchronized(lock) {
        @Suppress("UNCHECKED_CAST")
        pending += anim as Animation<Any>
    }

    fun tick(deltaSeconds: Double) = synchronized(lock) {
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
                is Vec2 -> AnimationUtil.lerp(from, to as Vec2, eased)
                is Spacing -> {
                    val t2 = to as Spacing
                    Spacing(
                        left = AnimationUtil.lerp(from.left, t2.left, eased),
                        top = AnimationUtil.lerp(from.top, t2.top, eased),
                        right = AnimationUtil.lerp(from.right, t2.right, eased),
                        bottom = AnimationUtil.lerp(from.bottom, t2.bottom, eased)
                    )
                }
                is Color -> from.lerp(to as Color, eased)
                is ScaleDimension -> lerpScaleDimension(from, to as ScaleDimension, eased)
                is ScaleDimension2 -> {
                    val t2 = to as ScaleDimension2
                    ScaleDimension2(
                        x = lerpScaleDimension(from.x, t2.x, eased),
                        y = lerpScaleDimension(from.y, t2.y, eased)
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
        animations.removeAll(toRemove.toSet())
        toRemove.forEach { it.onFinish() }

        animations += pending
        pending.clear()
    }

    private fun lerpScaleDimension(from: ScaleDimension, to: ScaleDimension, eased: Double): ScaleDimension {
        fun asFixedValue(d: ScaleDimension): Double? = when (d) {
            is ScaleDimension.One -> 1.0
            is ScaleDimension.Fixed -> d.value
            else -> null
        }

        val fromFixed = asFixedValue(from)
        val toFixed = asFixedValue(to)

        return when {
            fromFixed != null && toFixed != null ->
                ScaleDimension.Fixed(AnimationUtil.lerp(fromFixed, toFixed, eased))

            from is ScaleDimension.Fraction && to is ScaleDimension.Fraction ->
                ScaleDimension.Fraction(AnimationUtil.lerp(from.frac, to.frac, eased))

            from is ScaleDimension.ParentWidth && to is ScaleDimension.ParentWidth ->
                ScaleDimension.ParentWidth(AnimationUtil.lerp(from.frac, to.frac, eased))

            from is ScaleDimension.ParentHeight && to is ScaleDimension.ParentHeight ->
                ScaleDimension.ParentHeight(AnimationUtil.lerp(from.frac, to.frac, eased))

            else -> if (eased < 0.5) from else to
        }
    }
}

