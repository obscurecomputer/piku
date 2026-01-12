package computer.obscure.piku.client.scripting.api.camera

import computer.obscure.piku.client.camera.CameraAnimator
import computer.obscure.piku.client.camera.CinematicCamera
import computer.obscure.piku.common.scripting.api.LuaVec3Instance
import computer.obscure.piku.common.ui.events.ValueAnimation
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative
import net.minecraft.util.math.Vec3d

class LuaCameraAnimation : TwineNative() {
    val storedAnimations: MutableList<ValueAnimation<*>> = mutableListOf()

    @TwineNativeFunction
    fun play() {
        storedAnimations.forEach {
            CameraAnimator.animate(it)
        }
    }

    @TwineNativeFunction
    fun move(to: LuaVec3Instance, duration: Double, easing: String): LuaCameraAnimation {
        storedAnimations.add(
            ValueAnimation(
                durationSeconds = duration,
                easing = easing,
                getter = { CinematicCamera.pos },
                setter = { CinematicCamera.pos = it },
                to = Vec3d(to.x, to.y, to.z)
            )
        )

        return this
    }

}