package computer.obscure.piku.core.graphics

import java.awt.Color
import kotlin.math.max
import kotlin.math.pow
import kotlin.random.Random

@JvmInline
value class UIColor(
    val rgba: Int
) {
    val r: Int
        get() = (rgba shr 16) and 0xFF

    val g: Int
        get() = (rgba shr 8) and 0xFF

    val b: Int
        get() = rgba and 0xFF

    val a: Int
        get() = (rgba ushr 24) and 0xFF

    val hue: Float
        get() = hsvArray()[0] * 360f

    val saturation: Float
        get() = hsvArray()[1] * 100f

    val brightness: Float
        get() = hsvArray()[2] * 100f

    operator fun plus(other: UIColor): UIColor {
        return rgba(
            (r + other.r).coerceIn(0, 255),
            (g + other.g).coerceIn(0, 255),
            (b + other.b).coerceIn(0, 255),
            (a + other.a).coerceIn(0, 255)
        )
    }

    operator fun minus(other: UIColor): UIColor {
        return rgba(
            (r - other.r).coerceIn(0, 255),
            (g - other.g).coerceIn(0, 255),
            (b - other.b).coerceIn(0, 255),
            (a - other.a).coerceIn(0, 255)
        )
    }

    operator fun times(value: Float): UIColor {
        return rgba(
            (r * value).toInt().coerceIn(0, 255),
            (g * value).toInt().coerceIn(0, 255),
            (b * value).toInt().coerceIn(0, 255),
            a
        )
    }

    fun withRed(value: Int): UIColor =
        rgba(value, g, b, a)

    fun withGreen(value: Int): UIColor =
        rgba(r, value, b, a)

    fun withBlue(value: Int): UIColor =
        rgba(r, g, value, a)

    fun withAlpha(value: Int): UIColor =
        rgba(r, g, b, value)

    fun alpha(value: Int): UIColor =
        withAlpha(value)

    fun darken(amount: Float): UIColor =
        brighten(-amount)

    fun brighten(amount: Float): UIColor {
        val hsv = hsvArray()

        val brightness =
            (hsv[2] + amount)
                .coerceIn(0f, 1f)

        return hsv(
            hsv[0] * 360f,
            hsv[1] * 100f,
            brightness * 100f
        ).withAlpha(a)
    }

    fun saturate(amount: Float): UIColor {
        val hsv = hsvArray()

        val saturation =
            (hsv[1] + amount)
                .coerceIn(0f, 1f)

        return hsv(
            hsv[0] * 360f,
            saturation * 100f,
            hsv[2] * 100f
        ).withAlpha(a)
    }

    fun desaturate(amount: Float): UIColor =
        saturate(-amount)

    fun rotateHue(amount: Float): UIColor {
        val hsv = hsvArray()

        var hue = (hsv[0] * 360f + amount) % 360f

        if (hue < 0f) {
            hue += 360f
        }

        return hsv(
            hue,
            hsv[1] * 100f,
            hsv[2] * 100f
        ).withAlpha(a)
    }

    fun invert(): UIColor {
        return rgba(
            255 - r,
            255 - g,
            255 - b,
            a
        )
    }

    fun grayscale(): UIColor {
        val gray = (
                0.299f * r +
                        0.587f * g +
                        0.114f * b
                ).toInt()

        return rgba(gray, gray, gray, a)
    }

    fun lerp(other: UIColor, t: Float): UIColor {

        fun lerp(a: Int, b: Int): Int {
            return (a + ((b - a) * t))
                .toInt()
                .coerceIn(0, 255)
        }

        return rgba(
            lerp(r, other.r),
            lerp(g, other.g),
            lerp(b, other.b),
            lerp(a, other.a)
        )
    }

    fun mix(other: UIColor, t: Float): UIColor =
        lerp(other, t)

    fun multiply(other: UIColor): UIColor {
        return rgba(
            r * other.r / 255,
            g * other.g / 255,
            b * other.b / 255,
            a * other.a / 255
        )
    }

    fun screen(other: UIColor): UIColor {
        return rgba(
            255 - ((255 - r) * (255 - other.r) / 255),
            255 - ((255 - g) * (255 - other.g) / 255),
            255 - ((255 - b) * (255 - other.b) / 255),
            a
        )
    }

    fun overlay(other: UIColor): UIColor {

        fun overlay(a: Int, b: Int): Int {
            return if (a < 128) {
                (2 * a * b / 255)
            } else {
                255 - (2 * (255 - a) * (255 - b) / 255)
            }
        }

        return rgba(
            overlay(r, other.r),
            overlay(g, other.g),
            overlay(b, other.b),
            a
        )
    }

    fun luminance(): Float {

        fun channel(c: Int): Float {
            val v = c / 255f

            return if (v <= 0.03928f) {
                v / 12.92f
            } else {
                ((v + 0.055f) / 1.055f)
                    .toDouble()
                    .pow(2.4)
                    .toFloat()
            }
        }

        val rr = channel(r)
        val gg = channel(g)
        val bb = channel(b)

        return (
                0.2126f * rr +
                        0.7152f * gg +
                        0.0722f * bb
                )
    }

    fun brightnessValue(): Float {
        return max(
            r / 255f,
            max(g / 255f, b / 255f)
        )
    }

    fun isDark(): Boolean =
        luminance() < 0.5f

    fun isLight(): Boolean =
        !isDark()

    fun complementary(): UIColor =
        rotateHue(180f)

    fun copy(): UIColor =
        UIColor(rgba)

    fun hex(): String =
        "#%08X".format(rgba)

    override fun toString(): String {
        return buildString {
            append("UIColor(")
            append("r=$r")
            append(", g=$g")
            append(", b=$b")
            append(", a=$a")
            append(", hex=${hex()}")
            append(")")
        }
    }

    private fun hsvArray(): FloatArray {
        return Color.RGBtoHSB(r, g, b, null)
    }

    companion object {

        val WHITE = rgb(255, 255, 255)
        val BLACK = rgb(0, 0, 0)

        val RED = rgb(255, 0, 0)
        val GREEN = rgb(0, 255, 0)
        val BLUE = rgb(0, 0, 255)

        fun rgb(r: Int, g: Int, b: Int): UIColor {
            return rgba(r, g, b, 255)
        }

        fun rgba(r: Int, g: Int, b: Int, a: Int): UIColor {
            return UIColor(
                ((a and 0xFF) shl 24) or
                        ((r and 0xFF) shl 16) or
                        ((g and 0xFF) shl 8) or
                        (b and 0xFF)
            )
        }

        fun int(value: Int): UIColor {
            return UIColor(value)
        }

        fun hex(value: String): UIColor {
            val clean = value.removePrefix("#")

            val rgba = when (clean.length) {
                6 -> (0xFF shl 24) or clean.toLong(16).toInt()
                8 -> clean.toLong(16).toInt()
                else -> error("Invalid hex color: $value")
            }

            return UIColor(rgba)
        }

        fun hsv(h: Float, s: Float, v: Float): UIColor {
            val rgb = Color.HSBtoRGB(
                (h % 360f) / 360f,
                (s / 100f).coerceIn(0f, 1f),
                (v / 100f).coerceIn(0f, 1f)
            )

            return UIColor(rgb or (0xFF shl 24))
        }

        fun hsl(h: Float, s: Float, l: Float): UIColor {
            return UIColor(hslToRgb(h, s, l))
        }

        fun random(): UIColor {
            return rgb(
                Random.nextInt(256),
                Random.nextInt(256),
                Random.nextInt(256)
            )
        }

        fun rainbow(time: Float): UIColor {
            return hsv(
                (time * 360f) % 360f,
                100f,
                100f
            )
        }

        private fun hslToRgb(h: Float, s: Float, l: Float): Int {
            val hh = ((h % 360f) + 360f) % 360f / 360f
            val ss = (s / 100f).coerceIn(0f, 1f)
            val ll = (l / 100f).coerceIn(0f, 1f)

            if (ss <= 0f) {
                val gray = (ll * 255f).toInt()

                return (0xFF shl 24) or
                        (gray shl 16) or
                        (gray shl 8) or
                        gray
            }

            val q = if (ll < 0.5f)
                ll * (1f + ss)
            else
                ll + ss - ll * ss

            val p = 2f * ll - q

            fun hueToRgb(t: Float): Float {
                var tt = t

                if (tt < 0f) tt += 1f
                if (tt > 1f) tt -= 1f

                return when {
                    tt < 1f / 6f -> p + (q - p) * 6f * tt
                    tt < 1f / 2f -> q
                    tt < 2f / 3f -> p + (q - p) * (2f / 3f - tt) * 6f
                    else -> p
                }
            }

            val r = (hueToRgb(hh + 1f / 3f) * 255f).toInt()
            val g = (hueToRgb(hh) * 255f).toInt()
            val b = (hueToRgb(hh - 1f / 3f) * 255f).toInt()

            return (0xFF shl 24) or
                    (r shl 16) or
                    (g shl 8) or
                    b
        }
    }
}