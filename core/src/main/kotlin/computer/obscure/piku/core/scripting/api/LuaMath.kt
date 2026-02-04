package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative

class LuaMath : TwineNative() {
    @TwineNativeFunction
    fun clamp(x: Double, min: Double, max: Double): Double {
        if (x < min) return min
        if (x > max) return max
        return x
    }
}