package computer.obscure.piku.core.classes

import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineProperty
import me.znotchill.kiwi.generated.Vec3

data class Pos(
    @TwineProperty
    val vec: Vec3,
    @TwineProperty
    val pitch: Float,
    @TwineProperty
    val yaw: Float
) : TwineNative("pos")
