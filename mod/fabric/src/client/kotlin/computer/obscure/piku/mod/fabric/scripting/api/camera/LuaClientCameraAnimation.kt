package computer.obscure.piku.mod.fabric.scripting.api.camera

import computer.obscure.piku.mod.fabric.animation.Animation
import computer.obscure.piku.mod.fabric.ClientState
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.scripting.api.animation.LuaAnimatable
import computer.obscure.twine.LuaCallback
import computer.obscure.twine.annotations.TwineFunction
import me.znotchill.kiwi.generated.Vec3

class LuaClientCameraAnimation : LuaAnimatable() {
    @TwineFunction
    fun fov(
        to: Float,
        duration: Double,
        easing: String,
        onFinish: LuaCallback? = null
    ): LuaClientCameraAnimation {
        val anim =
            Animation(
                targetId = "client_fov",
                durationSeconds = duration,
                easing = easing,
                to = to,
                getter = { ClientState.vanillaFov },
                setter = { ClientState.animatedFov = it },
                onStart = {
                    ClientState.fovControlled = true
                },
                onFinish = {
                    ClientState.fovControlled = false
                    ClientState.animatedFov = to
                    if (!PikuClient.engine!!.engine.closed)
                        onFinish?.invoke()
                }
            )
        queue.add(anim)
        return this
    }

    @TwineFunction
    fun rotate(
        to: Vec3,
        duration: Double,
        easing: String,
        onFinish: LuaCallback? = null
    ): LuaClientCameraAnimation {
        queue.add(
            Animation(
                durationSeconds = duration,
                easing = easing,
                to = to,
                getter = { ClientState.rotation },
                setter = { ClientState.rotation = it },
                onFinish = {
                    if (!PikuClient.engine!!.engine.closed)
                        onFinish?.invoke()
                }
            )
        )
        return this
    }
}