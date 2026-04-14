package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative
import kotlin.math.*

class LuaMath : TwineNative("math") {
    @TwineFunction
    fun clamp(x: Double, min: Double, max: Double): Double = x.coerceIn(min, max)

    @TwineFunction
    fun lerp(a: Double, b: Double, t: Double): Double = Companion.lerp(a, b, t)

    @TwineFunction
    fun remap(v: Double, inMin: Double, inMax: Double, outMin: Double, outMax: Double): Double {
        return outMin + (v - inMin) * (outMax - outMin) / (inMax - inMin)
    }

    @TwineFunction
    fun roundTo(x: Double, step: Double): Double = round(x / step) * step

    @TwineFunction
    fun truncateTo(x: Double, decimals: Int): Double {
        val factor = 10.0.pow(decimals)
        return truncate(x * factor) / factor
    }
    @TwineFunction
    fun truncTo(x: Double, decimals: Int): Double {
        return truncateTo(x, decimals)
    }
    @TwineFunction
    fun trunc(x: Double, decimals: Int): Double {
        return truncateTo(x, decimals)
    }

    @TwineFunction
    fun approx(a: Double, b: Double, epsilon: Double): Boolean = abs(a - b) < epsilon

    @TwineFunction
    fun rad(x: Double): Double = Math.toRadians(x)

    @TwineFunction
    fun deg(x: Double): Double = Math.toDegrees(x)

    @TwineFunction
    fun sin(x: Double): Double = kotlin.math.sin(x)

    @TwineFunction
    fun cos(x: Double): Double = kotlin.math.cos(x)

    @TwineFunction
    fun abs(x: Double): Double = kotlin.math.abs(x)

    @TwineFunction
    fun sqrt(x: Double): Double = kotlin.math.sqrt(x)

    @TwineFunction
    fun floor(x: Double): Double = kotlin.math.floor(x)

    @TwineFunction
    fun ceil(x: Double): Double = kotlin.math.ceil(x)

    @TwineFunction
    fun trunc(x: Double): Double = kotlin.math.truncate(x)

    companion object {
        fun lerp(a: Double, b: Double, t: Double): Double = a + (b - a) * t
    }
}