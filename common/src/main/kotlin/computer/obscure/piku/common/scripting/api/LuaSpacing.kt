package computer.obscure.piku.common.scripting.api

import computer.obscure.piku.common.ui.classes.Spacing
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative

class LuaSpacing : TwineNative("spacing") {
//    @TwineOverload
//    @TwineNativeFunction
//    fun of(all: Float): LuaSpacingInstance {
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
//    fun of(x: Float, y: Float): LuaSpacingInstance {
//        return LuaSpacingInstance(
//            x = x,
//            y = y
//        )
//    }

//    @TwineOverload
    @TwineNativeFunction
    fun of(left: Float, top: Float, right: Float, bottom: Float): LuaSpacingInstance {
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
                bottom = spacing.bottom,
                x = spacing.x,
                y = spacing.y
            )
        }
    }
}

class LuaSpacingInstance(
    var left: Float = 0f,
    var top: Float = 0f,
    var right: Float = 0f,
    var bottom: Float = 0f,
    var x: Float = 0f,
    var y: Float = 0f
) : TwineNative() {

    @TwineNativeProperty("left")
    var leftValue: Float
        get() = left
        set(value) { left = value }

    @TwineNativeProperty("top")
    var topValue: Float
        get() = top
        set(value) { top = value }

    @TwineNativeProperty("right")
    var rightValue: Float
        get() = right
        set(value) { right = value }

    @TwineNativeProperty("bottom")
    var bottomValue: Float
        get() = bottom
        set(value) { bottom = value }

    @TwineNativeProperty("x")
    var xValue: Float
        get() = x
        set(value) {
            x = value
            left = value
            right = value
        }

    @TwineNativeProperty("y")
    var yValue: Float
        get() = y
        set(value) {
            y = value
            top = value
            bottom = value
        }

    fun toSpacing(): Spacing {
        return Spacing(
            left = left,
            top = top,
            right = right,
            bottom = bottom,
            x = x,
            y = y
        )
    }

    @TwineNativeFunction("tostring")
    override fun toString(): String {
        return "spacing[left=$left, top=$top, right=$right, bottom=$bottom]"
    }
}
