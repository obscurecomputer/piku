package computer.obscure.piku.mod.fabric.scripting.api.camera

import computer.obscure.piku.mod.fabric.animation.Animation
import computer.obscure.piku.mod.fabric.animation.AnimationManager
import computer.obscure.piku.core.scripting.api.LuaVec3
import computer.obscure.piku.core.scripting.api.LuaVec3Instance
import computer.obscure.piku.mod.fabric.ClientState
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty

class LuaClientCamera : TwineNative() {
    @TwineFunction
    fun lockFov() {
        ClientState.lockFov = true
    }
    @TwineFunction
    fun unlockFov() {
        ClientState.lockFov = false
        ClientState.fovControlled = false
        ClientState.currentFov = -1f
    }

    @TwineFunction
    fun fov(to: Float) {
        AnimationManager.animate(
            Animation.instant(
                to = to,
                getter = { ClientState.currentFov },
                setter = { ClientState.currentFov = it; ClientState.targetFov = it }
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
                getter = { ClientState.rotation },
                setter = { ClientState.rotation = it }
            )
        )
    }

    @TwineProperty
    val rotation: LuaVec3Instance
        get() {
            return LuaVec3.fromVec3(ClientState.rotation)
        }

    @TwineFunction
    fun animate(): LuaClientCameraAnimation {
        return LuaClientCameraAnimation()
    }
}