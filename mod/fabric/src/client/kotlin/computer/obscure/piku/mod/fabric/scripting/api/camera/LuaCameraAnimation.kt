package computer.obscure.piku.mod.fabric.scripting.api.camera

import computer.obscure.piku.core.classes.Vec3
import computer.obscure.piku.core.scripting.api.LuaVec3Instance
import computer.obscure.piku.core.ui.events.ValueAnimation
import computer.obscure.piku.mod.fabric.camera.CameraAnimator
import computer.obscure.piku.mod.fabric.camera.CinematicCamera
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative

class LuaCameraAnimation : TwineNative() {
    val storedAnimations: MutableList<ValueAnimation<*>> = mutableListOf()

    @TwineFunction
    fun play() {
        storedAnimations.forEach {
            CameraAnimator.animate(it)
        }
    }

    @TwineFunction
    fun move(to: LuaVec3Instance, duration: Double, easing: String): LuaCameraAnimation {
        storedAnimations.add(
            ValueAnimation(
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
            ValueAnimation(
                durationSeconds = duration,
                easing = easing,
                getter = {
                    Vec3(
                        CinematicCamera.pitch,
                        CinematicCamera.yaw,
                        CinematicCamera.roll
                    )
                },
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