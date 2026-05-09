package computer.obscure.piku.mod.fabric.scripting.api.camera

import computer.obscure.piku.core.animation.Animation
import computer.obscure.piku.core.animation.AnimationManager
import computer.obscure.piku.core.classes.Vec3
import computer.obscure.piku.core.scripting.api.LuaVec3Instance
import computer.obscure.piku.mod.fabric.camera.CinematicCamera
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative

class LuaCameraAnimation : TwineNative() {
    val storedAnimations: MutableList<Animation<*>> = mutableListOf()

    @TwineFunction
    fun play() {
        storedAnimations.forEach {
            AnimationManager.animate(it)
        }
    }

    @TwineFunction
    fun move(to: LuaVec3Instance, duration: Double, easing: String): LuaCameraAnimation {
        storedAnimations.add(
            Animation(
                durationSeconds = duration,
                easing = easing,
                getter = { CinematicCamera.pos },
                setter = { CinematicCamera.pos = it },
                to = Vec3(to.x, to.y, to.z)
            )
        )
        return this
    }

    @TwineFunction
    fun rotate(to: LuaVec3Instance, duration: Double, easing: String): LuaCameraAnimation {
        storedAnimations.add(
            Animation(
                durationSeconds = duration,
                easing = easing,
                getter = { Vec3(CinematicCamera.pitch, CinematicCamera.yaw, CinematicCamera.roll) },
                setter = { vec ->
                    CinematicCamera.pitch = vec.x
                    CinematicCamera.yaw = vec.y
                    CinematicCamera.roll = vec.z
                },
                to = Vec3(to.x, to.y, to.z)
            )
        )
        return this
    }
}