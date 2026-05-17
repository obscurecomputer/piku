package computer.obscure.piku.core.scripting.api

import computer.obscure.piku.core.classes.Spacing
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import computer.obscure.twine.TwineNative

class LuaSpacing : TwineNative("spacing") {

    @TwineFunction
    fun of(all: Double): LuaSpacingInstance =
        LuaSpacingInstance(all, all, all, all)

    fun all(v: Double) = of(v)

    @TwineFunction
    fun of(vertical: Double, horizontal: Double): LuaSpacingInstance =
        LuaSpacingInstance(
            top = vertical,
            bottom = vertical,
            left = horizontal,
            right = horizontal
        )

    fun verticalHorizontal(v: Double, h: Double) = of(v, h)

    @TwineFunction
    fun of(top: Double, horizontal: Double, bottom: Double): LuaSpacingInstance =
        LuaSpacingInstance(
            top = top,
            bottom = bottom,
            left = horizontal,
            right = horizontal
        )

    fun topHorizontalBottom(t: Double, h: Double, b: Double) =
        of(t, h, b)

    @TwineFunction
    fun of(
        top: Double,
        right: Double,
        bottom: Double,
        left: Double
    ): LuaSpacingInstance =
        LuaSpacingInstance(
            top = top,
            right = right,
            bottom = bottom,
            left = left
        )

    fun trbl(top: Double, right: Double, bottom: Double, left: Double) =
        of(top, right, bottom, left)

    companion object {
        fun fromSpacing(spacing: Spacing): LuaSpacingInstance =
            LuaSpacingInstance(
                left = spacing.left,
                top = spacing.top,
                right = spacing.right,
                bottom = spacing.bottom
            )
    }
}

class LuaSpacingInstance(
    @TwineProperty
    var left: Double = 0.0,
    @TwineProperty
    var top: Double = 0.0,
    @TwineProperty
    var right: Double = 0.0,
    @TwineProperty
    var bottom: Double = 0.0
) : TwineNative() {
    fun toSpacing(): Spacing =
        Spacing(left = left, top = top, right = right, bottom = bottom)

    @TwineFunction("tostring")
    override fun toString(): String {
        return "spacing[left=$left, top=$top, right=$right, bottom=$bottom]"
    }
}
