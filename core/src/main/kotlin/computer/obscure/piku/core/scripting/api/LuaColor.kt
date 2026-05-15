package computer.obscure.piku.core.scripting.api

import computer.obscure.piku.core.graphics.UIColor
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor

class LuaColor : TwineNative("color") {

    @TwineFunction
    fun rgb(r: Int, g: Int, b: Int): LuaColorInstance =
        LuaColorInstance(UIColor.rgb(r, g, b))

    @TwineFunction
    fun rgba(r: Int, g: Int, b: Int, a: Int): LuaColorInstance =
        LuaColorInstance(UIColor.rgba(r, g, b, a))

    @TwineFunction
    fun int(value: Int): LuaColorInstance =
        LuaColorInstance(UIColor.int(value))

    @TwineFunction
    fun hex(value: String): LuaColorInstance =
        LuaColorInstance(UIColor.hex(value))

    @TwineFunction
    fun hsv(h: Float, s: Float, v: Float): LuaColorInstance =
        LuaColorInstance(UIColor.hsv(h, s, v))

    @TwineFunction
    fun hsl(h: Float, s: Float, l: Float): LuaColorInstance =
        LuaColorInstance(UIColor.hsl(h, s, l))

    @TwineFunction
    fun rainbow(time: Float): LuaColorInstance =
        LuaColorInstance(UIColor.rainbow(time))

    @TwineFunction
    fun random(): LuaColorInstance =
        LuaColorInstance(UIColor.random())

    @TwineFunction
    fun white() = LuaColorInstance(UIColor.WHITE)

    @TwineFunction
    fun black() = LuaColorInstance(UIColor.BLACK)

    @TwineFunction
    fun red() = LuaColorInstance(UIColor.RED)

    @TwineFunction
    fun green() = LuaColorInstance(UIColor.GREEN)

    @TwineFunction
    fun blue() = LuaColorInstance(UIColor.BLUE)
}

class LuaColorInstance(
    val color: UIColor
) : TwineNative("color") {

    // properties

    @TwineProperty
    val r: Int
        get() = color.r

    @TwineProperty
    val g: Int
        get() = color.g

    @TwineProperty
    val b: Int
        get() = color.b

    @TwineProperty
    val a: Int
        get() = color.a

    @TwineProperty
    val value: Int
        get() = color.argb

    @TwineProperty
    val hue: Float
        get() = color.hue

    @TwineProperty
    val saturation: Float
        get() = color.saturation

    @TwineProperty
    val brightness: Float
        get() = color.brightness

    // conversions

    fun toUIColor(): UIColor =
        color

    fun toShadowColor(): ShadowColor =
        ShadowColor.shadowColor(r, g, b, a)

    fun toTextColor(): TextColor =
        TextColor.color(r, g, b)

    // modifiers

    @TwineFunction
    fun withRed(value: Int) =
        LuaColorInstance(color.withRed(value))

    @TwineFunction
    fun withGreen(value: Int) =
        LuaColorInstance(color.withGreen(value))

    @TwineFunction
    fun withBlue(value: Int) =
        LuaColorInstance(color.withBlue(value))

    @TwineFunction
    fun withAlpha(value: Int) =
        LuaColorInstance(color.withAlpha(value))

    @TwineFunction
    fun alpha(value: Int) =
        LuaColorInstance(color.alpha(value))

    @TwineFunction
    fun darken(amount: Float) =
        LuaColorInstance(color.darken(amount))

    @TwineFunction
    fun brighten(amount: Float) =
        LuaColorInstance(color.brighten(amount))

    @TwineFunction
    fun saturate(amount: Float) =
        LuaColorInstance(color.saturate(amount))

    @TwineFunction
    fun desaturate(amount: Float) =
        LuaColorInstance(color.desaturate(amount))

    @TwineFunction
    fun rotateHue(amount: Float) =
        LuaColorInstance(color.rotateHue(amount))

    @TwineFunction
    fun invert() =
        LuaColorInstance(color.invert())

    @TwineFunction
    fun grayscale() =
        LuaColorInstance(color.grayscale())

    @TwineFunction
    fun complementary() =
        LuaColorInstance(color.complementary())

    // blending

    @TwineFunction
    fun lerp(other: LuaColorInstance, t: Float) =
        LuaColorInstance(color.lerp(other.color, t))

    @TwineFunction
    fun mix(other: LuaColorInstance, t: Float) =
        LuaColorInstance(color.mix(other.color, t))

    @TwineFunction
    fun add(other: LuaColorInstance) =
        LuaColorInstance(color + other.color)

    @TwineFunction
    fun subtract(other: LuaColorInstance) =
        LuaColorInstance(color - other.color)

    @TwineFunction
    fun multiply(other: LuaColorInstance) =
        LuaColorInstance(color.multiply(other.color))

    @TwineFunction
    fun multiplyScalar(value: Float) =
        LuaColorInstance(color * value)

    @TwineFunction
    fun screen(other: LuaColorInstance) =
        LuaColorInstance(color.screen(other.color))

    @TwineFunction
    fun overlay(other: LuaColorInstance) =
        LuaColorInstance(color.overlay(other.color))

    // analysis

    @TwineFunction
    fun luminance(): Float =
        color.luminance()

    @TwineFunction
    fun brightnessValue(): Float =
        color.brightnessValue()

    @TwineFunction
    fun isDark(): Boolean =
        color.isDark()

    @TwineFunction
    fun isLight(): Boolean =
        color.isLight()

    // misc

    @TwineFunction
    fun copy() =
        LuaColorInstance(color.copy())

    @TwineFunction
    fun hex(): String =
        color.hex()

    @TwineFunction("tostring")
    override fun toString(): String =
        color.toString()
}