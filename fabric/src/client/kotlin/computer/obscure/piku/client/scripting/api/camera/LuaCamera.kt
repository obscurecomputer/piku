package computer.obscure.piku.client.scripting.api.camera

import computer.obscure.piku.client.camera.CinematicCamera
import computer.obscure.piku.client.scripting.engine.EngineError
import computer.obscure.piku.client.scripting.engine.EngineErrorCode
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative

class LuaCamera : TwineNative() {
    @TwineNativeFunction
    fun enable() {
        if (CinematicCamera.active)
            throw EngineError(
                EngineErrorCode.CAMERA_ACTIVE,
                "The cinematic camera is already active!"
            )
        CinematicCamera.enable()
    }

    @TwineNativeFunction
    fun disable() {
        if (!CinematicCamera.active)
            throw EngineError(
                EngineErrorCode.CAMERA_INACTIVE,
                "The cinematic camera is not active!"
            )
        CinematicCamera.disable()
    }

    @TwineNativeFunction
    fun toggle() {
        if (CinematicCamera.active) CinematicCamera.disable()
        else CinematicCamera.enable()
    }
}