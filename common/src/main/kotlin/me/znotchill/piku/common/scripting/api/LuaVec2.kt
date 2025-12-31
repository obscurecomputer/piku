package me.znotchill.piku.common.scripting.api

import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import dev.znci.twine.nativex.TwineNative
import me.znotchill.piku.common.classes.Vec2

class LuaVec2 : TwineNative("vec2") {
    @TwineNativeFunction
    fun of(x: Float, y: Float): LuaVec2Instance {
        return LuaVec2Instance(x, y)
    }

    companion object {
        fun fromVec2(vec2: Vec2): LuaVec2Instance {
            return LuaVec2Instance(
                vec2.x,
                vec2.y
            )
        }
    }
}

class LuaVec2Instance(
    var x: Float,
    var y: Float
) : TwineNative() {
    @TwineNativeProperty("x")
    var xCoord: Float
        get() = x
        set(value) { x = value }

    @TwineNativeProperty("y")
    var yCoord: Float
        get() = y
        set(value) { y = value }

    fun toVec2(): Vec2 {
        return Vec2(x = x, y = y)
    }

    @TwineNativeFunction("tostring")
    override fun toString(): String {
        return "vec2[x=$x, y=$y]"
    }
}