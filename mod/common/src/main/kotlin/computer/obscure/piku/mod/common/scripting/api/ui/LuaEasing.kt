package computer.obscure.piku.mod.common.scripting.api.ui

import computer.obscure.piku.mod.common.ui.UIRenderer
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative
import org.luaj.vm2.LuaValue

class LuaEasing : TwineNative("easing") {
    @TwineNativeFunction
    fun new(id: String, function: (time: Double) -> LuaValue): LuaEasingInstance {
        val instance = LuaEasingInstance(id) { t ->
            // avoids class org.luaj.vm2.LuaDouble cannot be cast to class java.lang.Number
            // in UIRenderer.kt:158
            function(t).todouble()
        }
        UIRenderer.registerEasing(instance)
        return instance
    }
}

class LuaEasingInstance(
    val internalId: String,
    val function: (time: Double) -> Double
) : TwineNative() {
    @TwineNativeProperty
    val id: String
        get() = internalId
}