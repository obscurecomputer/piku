package computer.obscure.piku.core.scripting.api

import computer.obscure.piku.core.ui.classes.Spacing
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import computer.obscure.twine.TwineNative

// This follows all the rules of CSS' padding order
// https://developer.mozilla.org/en-US/docs/Web/CSS/Reference/Properties/padding
class LuaSpacing : TwineNative("spacing") {
    @TwineFunction
    fun of(all: Double): LuaSpacingInstance {
        return LuaSpacingInstance(
            left = all,
            top = all,
            right = all,
            bottom = all
        )
    }

    @TwineFunction
    fun of(x: Double, y: Double): LuaSpacingInstance {
        return LuaSpacingInstance(
            left = y, right = y,
            top = x, bottom = x
        )
    }

    @TwineFunction
    fun of(x: Double, y: Double, z: Double): LuaSpacingInstance {
        return LuaSpacingInstance(
            top = x,
            right = y, left = y,
            bottom = z
        )
    }

    @TwineFunction
    fun of(
        top: Double,
        right: Double,
        bottom: Double,
        left: Double
    ): LuaSpacingInstance {
        return LuaSpacingInstance(
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
    }

    companion object {
        fun fromSpacing(spacing: Spacing): LuaSpacingInstance {
            return LuaSpacingInstance(
                left = spacing.left,
                top = spacing.top,
                right = spacing.right,
                bottom = spacing.bottom
            )
        }
    }
}

class LuaSpacingInstance(
    var left: Double = 0.0,
    var top: Double = 0.0,
    var right: Double = 0.0,
    var bottom: Double = 0.0
) : TwineNative() {

    @TwineProperty("left")
    var leftValue: Double
        get() = left
        set(value) { left = value }

    @TwineProperty("top")
    var topValue: Double
        get() = top
        set(value) { top = value }

    @TwineProperty("right")
    var rightValue: Double
        get() = right
        set(value) { right = value }

    @TwineProperty("bottom")
    var bottomValue: Double
        get() = bottom
        set(value) { bottom = value }

    fun toSpacing(): Spacing {
        return Spacing(
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
    }

    @TwineFunction("tostring")
    override fun toString(): String {
        return "spacing[left=$left, top=$top, right=$right, bottom=$bottom]"
    }
}
