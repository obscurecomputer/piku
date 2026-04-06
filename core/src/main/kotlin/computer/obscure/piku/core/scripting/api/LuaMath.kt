package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative

class LuaMath : TwineNative() {
    @TwineFunction
    fun clamp(x: Double, min: Double, max: Double): Double {
        if (x < min) return min
        if (x > max) return max
        return x
    }
}