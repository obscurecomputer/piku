package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import me.znotchill.kiwi.generated.Color

class LuaColor : TwineNative("color") {

    @TwineFunction
    fun rgb(r: Int, g: Int, b: Int): Color =
        Color.rgb(r, g, b)

    @TwineFunction
    fun rgba(r: Int, g: Int, b: Int, a: Int): Color =
        Color.rgba(r, g, b, a)

    @TwineFunction
    fun int(value: Int): Color =
        Color(value)

    @TwineFunction
    fun hex(value: String): Color =
        Color.hex(value)

    @TwineFunction
    fun hsv(h: Float, s: Float, v: Float): Color =
        Color.hsv(h, s, v)

    @TwineFunction
    fun hsl(h: Float, s: Float, l: Float): Color =
        Color.hsl(h, s, l)

    @TwineFunction
    fun rainbow(time: Float): Color =
        Color.rainbow(time)

    @TwineFunction
    fun random(): Color =
        Color.random()

    @TwineFunction
    fun white() = Color.WHITE

    @TwineFunction
    fun black() = Color.BLACK

    @TwineFunction
    fun red() = Color.RED

    @TwineFunction
    fun green() = Color.GREEN

    @TwineFunction
    fun blue() = Color.BLUE
}