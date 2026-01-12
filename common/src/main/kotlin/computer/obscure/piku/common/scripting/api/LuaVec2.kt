package computer.obscure.piku.common.scripting.api

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative
import computer.obscure.piku.common.classes.Vec2

class LuaVec2 : TwineNative("vec2") {
    @TwineNativeFunction
    fun of(x: Double, y: Double): LuaVec2Instance {
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
    var x: Double,
    var y: Double
) : TwineNative() {
    @TwineNativeProperty("x")
    var xCoord: Double
        get() = x
        set(value) { x = value }

    @TwineNativeProperty("y")
    var yCoord: Double
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