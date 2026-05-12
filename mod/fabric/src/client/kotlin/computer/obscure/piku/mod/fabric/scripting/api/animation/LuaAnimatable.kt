package computer.obscure.piku.mod.fabric.scripting.api.animation

import computer.obscure.piku.core.animation.Animation
import computer.obscure.piku.core.animation.AnimationManager
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import kotlin.collections.forEach

open class LuaAnimatable : TwineNative() {
    val queue: MutableList<Animation<*>> = mutableListOf()

    @TwineFunction
    fun play() {
        queue.forEach {
            AnimationManager.animate(it)
        }
    }
}