package computer.obscure.piku.mod.fabric.scripting.api.ui

import computer.obscure.piku.mod.fabric.ui.UIRenderer
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import computer.obscure.twine.TwineNative

class LuaEasing : TwineNative("easing") {
    @TwineFunction
    fun new(id: String): LuaEasingInstance {
        val instance = LuaEasingInstance(id)
        UIRenderer.registerEasing(instance)
        return instance
    }
}

class LuaEasingInstance(
    val internalId: String,
    var function: ((Double) -> Double) = { _ -> 0.0 }
) : TwineNative("easingInstance") {
    @TwineProperty
    val id: String get() = internalId

    fun withFunction(fn: (Double) -> Double): LuaEasingInstance {
        function = fn
        return this
    }

    fun evaluate(t: Double): Double = function.invoke(t)
}