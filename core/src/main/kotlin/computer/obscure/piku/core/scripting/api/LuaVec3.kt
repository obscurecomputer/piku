package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import computer.obscure.twine.TwineNative
import computer.obscure.piku.core.classes.Vec3

class LuaVec3 : TwineNative("vec3") {
    @TwineFunction
    fun of(x: Double, y: Double, z: Double): LuaVec3Instance {
        return LuaVec3Instance(x, y, z)
    }

    companion object {
        fun fromVec3(vec3: Vec3): LuaVec3Instance {
            return LuaVec3Instance(
                vec3.x,
                vec3.y,
                vec3.z
            )
        }

        val ZERO = LuaVec3Instance(0.0, 0.0, 0.0)
    }
}

class LuaVec3Instance(
    @TwineProperty
    var x: Double,
    @TwineProperty
    var y: Double,
    @TwineProperty
    var z: Double
) : TwineNative("vec3") {
    @TwineFunction
    fun set(x: Double, y: Double, z: Double): LuaVec3Instance {
        return LuaVec3Instance(x, y, z)
    }

    @TwineFunction
    fun add(x: Double, y: Double, z: Double): LuaVec3Instance {
        return LuaVec3Instance(
            this.x + x,
            this.y + y,
            this.z + z
        )
    }

    @TwineFunction
    fun sub(x: Double, y: Double, z: Double): LuaVec3Instance {
        return LuaVec3Instance(
            this.x - x,
            this.y - y,
            this.z - z
        )
    }

    @TwineFunction
    fun mul(x: Double, y: Double, z: Double): LuaVec3Instance {
        return LuaVec3Instance(
            this.x * x,
            this.y * y,
            this.z * z
        )
    }

    @TwineFunction
    fun div(x: Double, y: Double, z: Double): LuaVec3Instance {
        return LuaVec3Instance(
            this.x / x,
            this.y / y,
            this.z / z
        )
    }

    @TwineFunction
    fun mod(x: Double, y: Double, z: Double): LuaVec3Instance {
        return LuaVec3Instance(
            this.x % x,
            this.y % y,
            this.z % z
        )
    }

    @TwineFunction
    fun neg(): LuaVec3Instance {
        return LuaVec3Instance(
            -x,
            -y,
            -z
        )
    }

    fun toVec3(): Vec3 {
        return Vec3(
            x = x,
            y = y,
            z = z
        )
    }

    @TwineFunction("tostring")
    override fun toString(): String {
        return "vec3[x=$x, y=$y, z=$z]"
    }
}