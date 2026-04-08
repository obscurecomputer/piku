package computer.obscure.piku.mod.fabric.scripting.api.ui

import computer.obscure.piku.mod.fabric.ui.UIRenderer
import computer.obscure.twine.LuaCallback
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import computer.obscure.twine.TwineNative

class LuaEasing : TwineNative("easing") {
    @TwineFunction
    fun new(id: String, function: LuaCallback): LuaEasingInstance {
        val instance = LuaEasingInstance(id, function)
        UIRenderer.registerEasing(instance)
        return instance
    }
}

class LuaEasingInstance(
    val internalId: String,
    var function: LuaCallback
) : TwineNative() {
    @TwineProperty
    val id: String get() = internalId

    fun evaluate(t: Double): Double = function.call<Double>(t)
}