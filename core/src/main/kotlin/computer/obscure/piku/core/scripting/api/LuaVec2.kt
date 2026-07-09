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
    @TwineProperty
    var x: Double,
    @TwineProperty
    var y: Double
) : TwineNative("vec2") {
    @TwineFunction
    fun set(x: Double, y: Double): LuaVec2Instance {
        return LuaVec2Instance(x, y)
    }

    @TwineFunction
    fun add(x: Double, y: Double): LuaVec2Instance {
        return LuaVec2Instance(
            this.x + x,
            this.y + y
        )
    }

    @TwineFunction
    fun sub(x: Double, y: Double): LuaVec2Instance {
        return LuaVec2Instance(
            this.x - x,
            this.y - y
        )
    }

    @TwineFunction
    fun mul(x: Double, y: Double): LuaVec2Instance {
        return LuaVec2Instance(
            this.x * x,
            this.y * y
        )
    }

    @TwineFunction
    fun div(x: Double, y: Double): LuaVec2Instance {
        return LuaVec2Instance(
            this.x / x,
            this.y / y
        )
    }

    @TwineFunction
    fun mod(x: Double, y: Double): LuaVec2Instance {
        return LuaVec2Instance(
            this.x % x,
            this.y % y
        )
    }

    @TwineFunction
    fun neg(): LuaVec2Instance {
        return LuaVec2Instance(
            -x,
            -y
        )
    }

    fun toVec2(): Vec2 {
        return Vec2(x = x, y = y)
    }

    @TwineFunction("tostring")
    override fun toString(): String {
        return "vec2[x=$x, y=$y]"
    }
}