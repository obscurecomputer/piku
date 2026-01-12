package computer.obscure.piku.common.scripting.api

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative
import computer.obscure.piku.common.classes.Vec2
import computer.obscure.piku.common.classes.Vec3

class LuaVec3 : TwineNative("vec3") {
    @TwineNativeFunction
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
    }
}

class LuaVec3Instance(
    var x: Double,
    var y: Double,
    var z: Double
) : TwineNative() {
    @TwineNativeProperty("x")
    var xCoord: Double
        get() = x
        set(value) { x = value }

    @TwineNativeProperty("y")
    var yCoord: Double
        get() = y
        set(value) { y = value }

    @TwineNativeProperty("z")
    var zCoord: Double
        get() = z
        set(value) { z = value }

    fun toVec3(): Vec3 {
        return Vec3(x = x, y = y, z = z)
    }

    @TwineNativeFunction
    fun add(vec3: LuaVec3Instance): LuaVec3Instance {
        return LuaVec3Instance(
            x + vec3.x,
            y + vec3.y,
            z + vec3.z
        )
    }

    @TwineNativeFunction("tostring")
    override fun toString(): String {
        return "vec3[x=$x, y=$y, z=$z]"
    }
}