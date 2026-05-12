package computer.obscure.piku.mod.fabric.scripting.api.camera

import computer.obscure.piku.core.animation.Animation
import computer.obscure.piku.core.animation.AnimationManager
import computer.obscure.piku.core.scripting.api.LuaVec3
import computer.obscure.piku.core.scripting.api.LuaVec3Instance
import computer.obscure.piku.mod.fabric.Client
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty

class LuaClientCamera : TwineNative() {
    @TwineFunction
    fun lockFov() {
        Client.lockFov = true
    }
    @TwineFunction
    fun unlockFov() {
        Client.lockFov = false
        Client.fovControlled = false
        Client.currentFov = -1f
    }

    @TwineFunction
    fun fov(to: Float) {
        AnimationManager.animate(
            Animation.instant(
                to = to,
                getter = { Client.currentFov },
                setter = { Client.currentFov = it; Client.targetFov = it }
            )
        )
    }

    @TwineFunction
    fun rotate(
        to: LuaVec3Instance,
    ) {
        AnimationManager.animate(
            Animation.instant(
                to = to.toVec3(),
                getter = { Client.rotation },
                setter = { Client.rotation = it }
            )
        )
    }

    @TwineProperty
    val rotation: LuaVec3Instance
        get() {
            return LuaVec3.fromVec3(Client.rotation)
        }

    @TwineFunction
    fun animate(): LuaClientCameraAnimation {
        return LuaClientCameraAnimation()
    }
}