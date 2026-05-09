package computer.obscure.piku.core.classes

import computer.obscure.piku.core.scripting.api.LuaVec3
import computer.obscure.piku.core.serialization.PikuSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("vec3")
data class Vec3(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0
): PikuSerializable() {
    override val typeName = "vec3"
    override fun toLuaInstance() = LuaVec3.fromVec3(this)
    constructor(x: Number, y: Number, z: Number) : this(
        x.toDouble(),
        y.toDouble(),
        z.toDouble()
    )

    companion object {
        val ZERO = Vec3(0.0, 0.0, 0.0)
    }
}