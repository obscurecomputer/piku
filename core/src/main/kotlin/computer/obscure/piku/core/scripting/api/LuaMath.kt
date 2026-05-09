package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative
import kotlin.math.*
class LuaMath : TwineNative("math") {

    // clamp / ranges
    @TwineFunction fun clamp(x: Double, min: Double, max: Double): Double = x.coerceIn(min, max)
    @TwineFunction fun clamp01(x: Double): Double = x.coerceIn(0.0, 1.0)
    @TwineFunction fun wrap(x: Double, min: Double, max: Double): Double {
        val range = max - min
        return if (range == 0.0) min else min + ((x - min) % range + range) % range
    }

    // interpolation
    @TwineFunction fun lerp(a: Double, b: Double, t: Double): Double = a + (b - a) * t
    @TwineFunction fun lerpUnclamped(a: Double, b: Double, t: Double): Double = a + (b - a) * t
    @TwineFunction fun lerpAngle(a: Double, b: Double, t: Double): Double {
        var delta = ((b - a) % 360 + 540) % 360 - 180
        return a + delta * t
    }
    @TwineFunction fun inverseLerp(a: Double, b: Double, v: Double): Double = if (a == b) 0.0 else (v - a) / (b - a)
    @TwineFunction fun smoothstep(a: Double, b: Double, t: Double): Double {
        val x = ((t - a) / (b - a)).coerceIn(0.0, 1.0)
        return x * x * (3 - 2 * x)
    }
    @TwineFunction fun smootherstep(a: Double, b: Double, t: Double): Double {
        val x = ((t - a) / (b - a)).coerceIn(0.0, 1.0)
        return x * x * x * (x * (x * 6 - 15) + 10)
    }
    @TwineFunction fun remap(v: Double, inMin: Double, inMax: Double, outMin: Double, outMax: Double): Double =
        outMin + (v - inMin) * (outMax - outMin) / (inMax - inMin)
    @TwineFunction fun moveTowards(current: Double, target: Double, maxDelta: Double): Double {
        val diff = target - current
        return if (abs(diff) <= maxDelta) target else current + sign(diff) * maxDelta
    }

    // rounding
    @TwineFunction fun round(x: Double): Double = kotlin.math.round(x).toDouble()
    @TwineFunction fun floor(x: Double): Double = kotlin.math.floor(x)
    @TwineFunction fun ceil(x: Double): Double = kotlin.math.ceil(x)
    @TwineFunction fun trunc(x: Double): Double = truncate(x)
    @TwineFunction fun trunc(x: Double, decimals: Int): Double = truncateTo(x, decimals)
    @TwineFunction fun truncateTo(x: Double, decimals: Int): Double {
        val factor = 10.0.pow(decimals)
        return truncate(x * factor) / factor
    }
    @TwineFunction fun roundTo(x: Double, step: Double): Double = kotlin.math.round(x / step) * step
    @TwineFunction fun snap(x: Double, step: Double): Double = kotlin.math.floor(x / step) * step

    // trig
    @TwineFunction fun sin(x: Double): Double = kotlin.math.sin(x)
    @TwineFunction fun cos(x: Double): Double = kotlin.math.cos(x)
    @TwineFunction fun tan(x: Double): Double = kotlin.math.tan(x)
    @TwineFunction fun asin(x: Double): Double = kotlin.math.asin(x)
    @TwineFunction fun acos(x: Double): Double = kotlin.math.acos(x)
    @TwineFunction fun atan(x: Double): Double = kotlin.math.atan(x)
    @TwineFunction fun atan2(y: Double, x: Double): Double = kotlin.math.atan2(y, x)
    @TwineFunction fun rad(x: Double): Double = Math.toRadians(x)
    @TwineFunction fun deg(x: Double): Double = Math.toDegrees(x)

    // Angles
    @TwineFunction fun normalizeAngle(angle: Double): Double =
        ((angle % 360) + 360) % 360
    @TwineFunction fun angleDiff(a: Double, b: Double): Double =
        ((b - a + 540) % 360) - 180
    @TwineFunction fun angleTowards(x1: Double, y1: Double, x2: Double, y2: Double): Double =
        Math.toDegrees(kotlin.math.atan2(y2 - y1, x2 - x1))

    // powers / roots
    @TwineFunction fun sqrt(x: Double): Double = kotlin.math.sqrt(x)
    @TwineFunction fun cbrt(x: Double): Double = kotlin.math.cbrt(x)
    @TwineFunction fun pow(x: Double, n: Double): Double = x.pow(n)
    @TwineFunction fun exp(x: Double): Double = kotlin.math.exp(x)
    @TwineFunction fun log(x: Double): Double = kotlin.math.ln(x)
    @TwineFunction fun log2(x: Double): Double = kotlin.math.log2(x)
    @TwineFunction fun log10(x: Double): Double = kotlin.math.log10(x)

    // sign / magnitude
    @TwineFunction fun abs(x: Double): Double = kotlin.math.abs(x)
    @TwineFunction fun sign(x: Double): Double = kotlin.math.sign(x)
    @TwineFunction fun min(a: Double, b: Double): Double = kotlin.math.min(a, b)
    @TwineFunction fun max(a: Double, b: Double): Double = kotlin.math.max(a, b)

    // distance / geometry
    @TwineFunction fun dist(x1: Double, y1: Double, x2: Double, y2: Double): Double =
        kotlin.math.sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2))
    @TwineFunction fun dist3(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Double =
        kotlin.math.sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2) + (z2 - z1).pow(2))
    @TwineFunction fun magnitude(x: Double, y: Double): Double = kotlin.math.sqrt(x * x + y * y)
    @TwineFunction fun magnitude3(x: Double, y: Double, z: Double): Double = kotlin.math.sqrt(x * x + y * y + z * z)
    @TwineFunction fun dot(x1: Double, y1: Double, x2: Double, y2: Double): Double = x1 * x2 + y1 * y2
    @TwineFunction fun cross2(x1: Double, y1: Double, x2: Double, y2: Double): Double = x1 * y2 - y1 * x2

    // misc
    @TwineFunction fun approx(a: Double, b: Double, epsilon: Double = 1e-6): Boolean =
        abs(a - b) < epsilon
    @TwineFunction fun fract(x: Double): Double =
        x - truncate(x)
    @TwineFunction fun pingpong(x: Double, length: Double): Double {
        val t = ((x % (length * 2)) + length * 2) % (length * 2)
        return if (t < length) t else length * 2 - t
    }
    @TwineFunction fun oscillate(x: Double, speed: Double): Double =
        (kotlin.math.sin(x * speed) + 1.0) / 2.0

    // constants
    @TwineFunction fun pi(): Double = PI
    @TwineFunction fun tau(): Double = PI * 2
    @TwineFunction fun e(): Double = E
    @TwineFunction fun huge(): Double = Double.MAX_VALUE
    @TwineFunction fun random(): Double = Math.random()
    @TwineFunction fun random(min: Double, max: Double): Double =
        min + Math.random() * (max - min)
    @TwineFunction fun randomInt(min: Int, max: Int): Int =
        (min..max).random()

    companion object {
        fun lerp(a: Double, b: Double, t: Double): Double =
            a + (b - a) * t
    }
}