package computer.obscure.piku.core.scripting.api

import computer.obscure.piku.core.ui.classes.UIColor
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor
import java.awt.Color
import kotlin.math.max
import kotlin.math.pow
import kotlin.random.Random

// color math is hell
class LuaColor : TwineNative("color") {
    @TwineFunction
    fun rgb(r: Int, g: Int, b: Int): LuaColorInstance {
        return LuaColorInstance(r, g, b)
    }

    @TwineFunction
    fun rgba(r: Int, g: Int, b: Int, a: Int): LuaColorInstance {
        return LuaColorInstance(r, g, b, a)
    }

    @TwineFunction
    fun int(rgba: Int): LuaColorInstance {
        return LuaColorInstance(rgba)
    }

    @TwineFunction
    fun hex(value: String): LuaColorInstance {
        val clean = value.removePrefix("#")

        val rgba = when (clean.length) {
            6 -> (0xFF shl 24) or clean.toLong(16).toInt()
            8 -> clean.toLong(16).toInt()
            else -> error("Invalid hex color: $value")
        }

        return LuaColorInstance(rgba)
    }

    @TwineFunction
    fun hsv(h: Float, s: Float, v: Float): LuaColorInstance {
        val rgb = Color.HSBtoRGB(
            (h % 360f) / 360f,
            (s / 100f).coerceIn(0f, 1f),
            (v / 100f).coerceIn(0f, 1f)
        )

        return LuaColorInstance(rgb or (0xFF shl 24))
    }

    @TwineFunction
    fun hsl(h: Float, s: Float, l: Float): LuaColorInstance {
        return LuaColorInstance(
            hslToRgb(h, s, l)
        )
    }

    @TwineFunction
    fun rainbow(time: Float): LuaColorInstance {
        return hsv(
            (time * 360f) % 360f,
            100f,
            100f
        )
    }

    @TwineFunction
    fun random(): LuaColorInstance {
        return rgb(
            Random.nextInt(256),
            Random.nextInt(256),
            Random.nextInt(256)
        )
    }

    @TwineFunction
    fun randomBright(): LuaColorInstance {
        return hsv(
            Random.nextFloat() * 360f,
            80f,
            100f
        )
    }

    @TwineFunction
    fun randomPastel(): LuaColorInstance {
        return hsv(
            Random.nextFloat() * 360f,
            30f,
            100f
        )
    }

    @TwineFunction fun white() = rgb(255, 255, 255)
    @TwineFunction fun black() = rgb(0, 0, 0)

    @TwineFunction fun red() = rgb(255, 0, 0)
    @TwineFunction fun green() = rgb(0, 255, 0)
    @TwineFunction fun blue() = rgb(0, 0, 255)

    @TwineFunction fun yellow() = rgb(255, 255, 0)
    @TwineFunction fun cyan() = rgb(0, 255, 255)
    @TwineFunction fun magenta() = rgb(255, 0, 255)

    @TwineFunction fun orange() = rgb(255, 165, 0)
    @TwineFunction fun pink() = rgb(255, 105, 180)
    @TwineFunction fun purple() = rgb(128, 0, 255)

    companion object {

        fun fromUIColor(uiColor: UIColor): LuaColorInstance {
            return LuaColorInstance(
                uiColor.r,
                uiColor.g,
                uiColor.b,
                uiColor.a
            )
        }

        fun fromShadowColor(shadowColor: ShadowColor): LuaColorInstance {
            return LuaColorInstance(
                shadowColor.red(),
                shadowColor.green(),
                shadowColor.blue(),
                shadowColor.alpha()
            )
        }

        fun hslToRgb(h: Float, s: Float, l: Float): Int {
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

class LuaColorInstance(
    rgba: Int
) : TwineNative("color") {

    private var rgba: Int = rgba

    constructor(
        r: Int,
        g: Int,
        b: Int,
        a: Int = 255
    ) : this(
        ((a and 0xFF) shl 24) or
                ((r and 0xFF) shl 16) or
                ((g and 0xFF) shl 8) or
                (b and 0xFF)
    )

    @TwineProperty
    val r: Int
        get() = (rgba shr 16) and 0xFF

    @TwineProperty
    val g: Int
        get() = (rgba shr 8) and 0xFF

    @TwineProperty
    val b: Int
        get() = rgba and 0xFF

    @TwineProperty
    val a: Int
        get() = (rgba ushr 24) and 0xFF

    @TwineProperty
    val value: Int
        get() = rgba

    private fun hsvArray(): FloatArray {
        return Color.RGBtoHSB(r, g, b, null)
    }

    @TwineProperty
    val hue: Float
        get() = hsvArray()[0] * 360f

    @TwineProperty
    val saturation: Float
        get() = hsvArray()[1] * 100f

    @TwineProperty
    val brightness: Float
        get() = hsvArray()[2] * 100f

    fun toUIColor(): UIColor =
        UIColor(r, g, b, a)

    fun toShadowColor(): ShadowColor =
        ShadowColor.shadowColor(r, g, b, a)

    fun toTextColor(): TextColor =
        TextColor.color(r, g, b)

    @TwineFunction
    fun withRed(value: Int): LuaColorInstance =
        LuaColorInstance(value, g, b, a)

    @TwineFunction
    fun withGreen(value: Int): LuaColorInstance =
        LuaColorInstance(r, value, b, a)

    @TwineFunction
    fun withBlue(value: Int): LuaColorInstance =
        LuaColorInstance(r, g, value, a)

    @TwineFunction
    fun withAlpha(value: Int): LuaColorInstance =
        LuaColorInstance(r, g, b, value)

    @TwineFunction
    fun alpha(value: Int): LuaColorInstance =
        withAlpha(value)

    @TwineFunction
    fun rotateHue(amount: Float): LuaColorInstance {
        val hsv = hsvArray()

        var hue = (hsv[0] * 360f + amount) % 360f

        if (hue < 0f) {
            hue += 360f
        }

        return LuaColor()
            .hsv(
                hue,
                hsv[1] * 100f,
                hsv[2] * 100f
            )
            .withAlpha(a)
    }

    @TwineFunction
    fun saturate(amount: Float): LuaColorInstance {
        val hsv = hsvArray()

        val saturation =
            (hsv[1] + amount)
                .coerceIn(0f, 1f)

        return LuaColor()
            .hsv(
                hsv[0] * 360f,
                saturation * 100f,
                hsv[2] * 100f
            )
            .withAlpha(a)
    }

    @TwineFunction
    fun desaturate(amount: Float): LuaColorInstance {
        return saturate(-amount)
    }

    @TwineFunction
    fun brighten(amount: Float): LuaColorInstance {
        val hsv = hsvArray()

        val brightness =
            (hsv[2] + amount)
                .coerceIn(0f, 1f)

        return LuaColor()
            .hsv(
                hsv[0] * 360f,
                hsv[1] * 100f,
                brightness * 100f
            )
            .withAlpha(a)
    }

    @TwineFunction
    fun darken(amount: Float): LuaColorInstance {
        return brighten(-amount)
    }

    @TwineFunction
    fun lerp(other: LuaColorInstance, t: Float): LuaColorInstance {

        fun lerp(a: Int, b: Int): Int {
            return (a + ((b - a) * t)).toInt()
                .coerceIn(0, 255)
        }

        return LuaColorInstance(
            lerp(r, other.r),
            lerp(g, other.g),
            lerp(b, other.b),
            lerp(a, other.a)
        )
    }

    @TwineFunction
    fun mix(other: LuaColorInstance, t: Float): LuaColorInstance {
        return lerp(other, t)
    }

    @TwineFunction
    fun add(other: LuaColorInstance): LuaColorInstance {
        return LuaColorInstance(
            (r + other.r).coerceIn(0, 255),
            (g + other.g).coerceIn(0, 255),
            (b + other.b).coerceIn(0, 255),
            (a + other.a).coerceIn(0, 255)
        )
    }

    @TwineFunction
    fun subtract(other: LuaColorInstance): LuaColorInstance {
        return LuaColorInstance(
            (r - other.r).coerceIn(0, 255),
            (g - other.g).coerceIn(0, 255),
            (b - other.b).coerceIn(0, 255),
            (a - other.a).coerceIn(0, 255)
        )
    }

    @TwineFunction
    fun multiply(other: LuaColorInstance): LuaColorInstance {
        return LuaColorInstance(
            (r * other.r / 255),
            (g * other.g / 255),
            (b * other.b / 255),
            (a * other.a / 255)
        )
    }

    @TwineFunction
    fun multiplyScalar(value: Float): LuaColorInstance {
        return LuaColorInstance(
            (r * value).toInt().coerceIn(0, 255),
            (g * value).toInt().coerceIn(0, 255),
            (b * value).toInt().coerceIn(0, 255),
            a
        )
    }

    @TwineFunction
    fun screen(other: LuaColorInstance): LuaColorInstance {
        return LuaColorInstance(
            255 - ((255 - r) * (255 - other.r) / 255),
            255 - ((255 - g) * (255 - other.g) / 255),
            255 - ((255 - b) * (255 - other.b) / 255),
            a
        )
    }

    @TwineFunction
    fun overlay(other: LuaColorInstance): LuaColorInstance {

        fun overlay(a: Int, b: Int): Int {
            return if (a < 128) {
                (2 * a * b / 255)
            } else {
                255 - (2 * (255 - a) * (255 - b) / 255)
            }
        }

        return LuaColorInstance(
            overlay(r, other.r),
            overlay(g, other.g),
            overlay(b, other.b),
            a
        )
    }

    @TwineFunction
    fun luminance(): Float {

        fun channel(c: Int): Float {
            val v = c / 255f

            return if (v <= 0.03928f) {
                v / 12.92f
            } else {
                ((v + 0.055f) / 1.055f).toDouble().pow(2.4).toFloat()
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

    @TwineFunction
    fun brightnessValue(): Float {
        return max(
            r / 255f,
            max(
                g / 255f,
                b / 255f
            )
        )
    }

    @TwineFunction
    fun isDark(): Boolean {
        return luminance() < 0.5f
    }

    @TwineFunction
    fun isLight(): Boolean {
        return !isDark()
    }

    @TwineFunction
    fun grayscale(): LuaColorInstance {
        val gray = (
                0.299f * r +
                        0.587f * g +
                        0.114f * b
                ).toInt()

        return LuaColorInstance(
            gray,
            gray,
            gray,
            a
        )
    }

    @TwineFunction
    fun invert(): LuaColorInstance {
        return LuaColorInstance(
            255 - r,
            255 - g,
            255 - b,
            a
        )
    }

    @TwineFunction
    fun complementary(): LuaColorInstance {
        return rotateHue(180f)
    }

    @TwineFunction
    fun copy(): LuaColorInstance {
        return LuaColorInstance(rgba)
    }

    @TwineFunction
    fun hex(): String {
        return "#%08X".format(rgba)
    }

    @TwineFunction("tostring")
    override fun toString(): String {

        return buildString {
            append("color[")
            append("r=$r")
            append(", g=$g")
            append(", b=$b")
            append(", a=$a")
            append(", hex=${hex()}")
            append("]")
        }
    }
}