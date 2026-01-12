package computer.obscure.piku.common.scripting.api

import computer.obscure.piku.common.ui.classes.Spacing
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative

class LuaSpacing : TwineNative("spacing") {
//    @TwineOverload
//    @TwineNativeFunction
//    fun of(all: Double): LuaSpacingInstance {
//        return LuaSpacingInstance(
//            left = all,
//            top = all,
//            right = all,
//            bottom = all
//        )
//    }
//
//    @TwineOverload
//    @TwineNativeFunction
//    fun of(x: Double, y: Double): LuaSpacingInstance {
//        return LuaSpacingInstance(
//            x = x,
//            y = y
//        )
//    }

//    @TwineOverload
    @TwineNativeFunction
    fun of(left: Double, top: Double, right: Double, bottom: Double): LuaSpacingInstance {
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

    @TwineNativeProperty("left")
    var leftValue: Double
        get() = left
        set(value) { left = value }

    @TwineNativeProperty("top")
    var topValue: Double
        get() = top
        set(value) { top = value }

    @TwineNativeProperty("right")
    var rightValue: Double
        get() = right
        set(value) { right = value }

    @TwineNativeProperty("bottom")
    var bottomValue: Double
        get() = bottom
        set(value) { bottom = value }

    fun toSpacing(): Spacing {
        println(this.toString())
        return Spacing(
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
    }

    @TwineNativeFunction("tostring")
    override fun toString(): String {
        return "spacing[left=$left, top=$top, right=$right, bottom=$bottom]"
    }
}
