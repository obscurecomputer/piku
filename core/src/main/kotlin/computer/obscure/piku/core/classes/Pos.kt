package computer.obscure.piku.core.classes

import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import kotlinx.serialization.Serializable
import me.znotchill.kiwi.generated.Vec3

@Serializable
data class Pos(
    @TwineProperty val vec: Vec3,
    @TwineProperty val pitch: Float,
    @TwineProperty val yaw: Float
) : TwineNative("pos") {

    @TwineFunction
    fun add(offset: Vec3): Pos = copy(vec = vec + offset)

    @TwineFunction
    fun add(other: Pos): Pos = copy(vec = vec + other.vec)

    @TwineFunction
    fun forward(): Vec3 {
        val p = Math.toRadians(pitch.toDouble())
        val y = Math.toRadians(yaw.toDouble())
        return Vec3(
            -kotlin.math.sin(y) * kotlin.math.cos(p),
            -kotlin.math.sin(p),
            kotlin.math.cos(y) * kotlin.math.cos(p)
        )
    }
}