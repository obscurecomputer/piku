package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative
import kotlin.math.abs
import kotlin.math.round

class LuaMath : TwineNative("math") {
    @TwineFunction
    fun clamp(x: Double, min: Double, max: Double): Double {
        if (x < min) return min
        if (x > max) return max
        return x
    }

    @TwineFunction
    fun lerp(a: Double, b: Double, t: Double): Double = LuaMath.lerp(a, b, t)

    @TwineFunction
    fun remap(v: Double, inMin: Double, inMax: Double, outMin: Double, outMax: Double): Double {
        return outMin + (v - inMin) * (outMax - outMin) / (inMax - inMin)
    }

    @TwineFunction
    fun roundTo(x: Double, step: Double): Double {
        return round(x / step) * step
    }

    @TwineFunction
    fun approx(a: Double, b: Double, epsilon: Double): Boolean {
        return abs(a - b) < epsilon
    }

    companion object {
        fun lerp(a: Double, b: Double, t: Double): Double = a + (b - a) * t
    }
}