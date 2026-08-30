package computer.obscure.piku.mod.fabric.scripting.old.animation

import computer.obscure.piku.mod.fabric.animation.Animation
import computer.obscure.piku.mod.fabric.animation.AnimationManager
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