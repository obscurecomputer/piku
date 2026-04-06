package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import computer.obscure.twine.TwineNative
import computer.obscure.piku.core.classes.Vec2

class LuaVec2 : TwineNative("vec2") {
    @TwineFunction
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
    @TwineProperty("x")
    var xCoord: Double
        get() = x
        set(value) { x = value }

    @TwineProperty("y")
    var yCoord: Double
        get() = y
        set(value) { y = value }

    fun toVec2(): Vec2 {
        return Vec2(x = x, y = y)
    }

    @TwineFunction("tostring")
    override fun toString(): String {
        return "vec2[x=$x, y=$y]"
    }
}