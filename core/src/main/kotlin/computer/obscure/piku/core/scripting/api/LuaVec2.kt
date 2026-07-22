package computer.obscure.piku.core.scripting.api

import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative
import me.znotchill.kiwi.generated.Vec2

class LuaVec2 : TwineNative("vec2") {
    @TwineFunction
    fun of(x: Double, y: Double): Vec2 {
        return Vec2(x, y)
    }

    companion object {
        fun fromVec2(vec2: Vec2): Vec2 {
            return Vec2(
                vec2.x,
                vec2.y
            )
        }
    }
}