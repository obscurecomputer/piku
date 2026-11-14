package computer.obscure.piku.mod.fabric.scripting.api.camera

import computer.obscure.piku.core.scripting.engine.EngineError
import computer.obscure.piku.core.scripting.engine.EngineErrorCode
import computer.obscure.piku.mod.fabric.camera.CinematicCamera
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative
import me.znotchill.kiwi.generated.Vec3

class LuaCinematicCamera : TwineNative() {
    @TwineFunction
    fun enable() {
        if (CinematicCamera.active)
            throw EngineError(
                EngineErrorCode.CAMERA_ACTIVE,
                "The cinematic camera is already active!"
            )
        CinematicCamera.enable()
    }

    @TwineFunction
    fun disable() {
        if (!CinematicCamera.active)
            throw EngineError(
                EngineErrorCode.CAMERA_INACTIVE,
                "The cinematic camera is not active!"
            )
        CinematicCamera.disable()
    }

    @TwineFunction
    fun toggle() {
        if (CinematicCamera.active) CinematicCamera.disable()
        else CinematicCamera.enable()
    }

    @TwineFunction
    fun move(to: Vec3) {
        CinematicCamera.pos = to
    }

    @TwineFunction
    fun rotate(to: Vec3) {
        CinematicCamera.rotation = to
    }

    @TwineFunction
    fun animate(): LuaCameraAnimation {
        return LuaCameraAnimation()
    }
}