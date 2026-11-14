package computer.obscure.piku.mod.fabric.scripting.api.camera

import computer.obscure.piku.mod.fabric.animation.Animation
import computer.obscure.piku.mod.fabric.animation.AnimationManager
import computer.obscure.piku.mod.fabric.ClientState
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty
import me.znotchill.kiwi.generated.Vec3

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
        to: Vec3,
    ) {
        AnimationManager.animate(
            Animation.instant(
                to = to,
                getter = { ClientState.rotation },
                setter = { ClientState.rotation = it }
            )
        )
    }

    @TwineProperty
    val rotation: Vec3
        get() {
            return ClientState.rotation
        }

    @TwineFunction
    fun animate(): LuaClientCameraAnimation {
        return LuaClientCameraAnimation()
    }
}