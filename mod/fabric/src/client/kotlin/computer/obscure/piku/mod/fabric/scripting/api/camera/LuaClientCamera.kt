package computer.obscure.piku.mod.fabric.scripting.api.camera

import computer.obscure.piku.core.scripting.api.LuaVec3
import computer.obscure.piku.core.scripting.api.LuaVec3Instance
import computer.obscure.piku.mod.fabric.Client
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.animation.Animation
import computer.obscure.piku.mod.fabric.animation.AnimationManager
import computer.obscure.twine.LuaCallback
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty

class LuaClientCamera : TwineNative() {
    @TwineFunction
    fun rotate(
        to: LuaVec3Instance,
        duration: Double,
        easing: String = "linear",
        onFinish: LuaCallback? = null
    ) {
        AnimationManager.animate(
            Animation(
                durationSeconds = duration,
                easing = easing,
                to = to.toVec3(),
                getter = { Client.rotation },
                setter = { Client.rotation = it },
                onFinish = {
                    if (!PikuClient.engine!!.twine.closed)
                        onFinish?.invoke()
                }
            )
        )
    }

    @TwineProperty
    val rotation: LuaVec3Instance
        get() {
//            val player = Minecraft.getInstance().player
//                ?: return LuaVec3.ZERO
//
//            return LuaVec3Instance(
//                player.xRot.toDouble(),
//                player.yRot.toDouble(),
//                0.0
//            )
            return LuaVec3.fromVec3(Client.rotation)
        }
}