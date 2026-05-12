package computer.obscure.piku.mod.fabric.scripting.api.camera

import computer.obscure.piku.core.animation.Animation
import computer.obscure.piku.core.scripting.api.LuaVec3Instance
import computer.obscure.piku.mod.fabric.Client
import computer.obscure.piku.mod.fabric.PikuClient
import computer.obscure.piku.mod.fabric.scripting.api.animation.LuaAnimatable
import computer.obscure.twine.LuaCallback
import computer.obscure.twine.annotations.TwineFunction

class LuaClientCameraAnimation : LuaAnimatable() {
    @TwineFunction
    fun fov(
        to: Float,
        duration: Double,
        easing: String,
        onFinish: LuaCallback? = null
    ): LuaClientCameraAnimation {
        queue.add(
            Animation(
                targetId = "client_fov",
                durationSeconds = duration,
                easing = easing,
                to = to,
                getter = { Client.currentFov },
                setter = { Client.currentFov = it },
                onStart = { Client.fovControlled = true },
                onFinish = {
                    Client.fovControlled = false
                    Client.currentFov = to
                    Client.targetFov = to
                    if (!PikuClient.engine!!.twine.closed)
                        onFinish?.invoke()
                }
            )
        )
        return this
    }

    @TwineFunction
    fun rotate(
        to: LuaVec3Instance,
        duration: Double,
        easing: String,
        onFinish: LuaCallback? = null
    ): LuaClientCameraAnimation {
        queue.add(
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
        return this
    }
}