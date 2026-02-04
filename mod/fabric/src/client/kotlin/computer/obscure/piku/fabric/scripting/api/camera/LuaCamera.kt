package computer.obscure.piku.fabric.scripting.api.camera

import computer.obscure.piku.fabric.camera.CinematicCamera
import computer.obscure.piku.common.classes.Vec3
import computer.obscure.piku.common.scripting.api.LuaVec3Instance
import computer.obscure.piku.common.scripting.engine.EngineError
import computer.obscure.piku.common.scripting.engine.EngineErrorCode
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative
import net.minecraft.util.math.Vec3d

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

    @TwineNativeFunction
    fun move(to: LuaVec3Instance) {
        CinematicCamera.pos = Vec3d(to.x, to.y, to.z)
    }

    @TwineNativeFunction
    fun rotate(to: LuaVec3Instance) {
        CinematicCamera.rotation = Vec3(to.x, to.y, to.z)
    }

    @TwineNativeFunction
    fun animate(): LuaCameraAnimation {
        return LuaCameraAnimation()
    }

    companion object {
        internal fun requireActive() {
            if (!CinematicCamera.active) {
                throw EngineError(
                    EngineErrorCode.CAMERA_INACTIVE,
                    "The cinematic camera is not active!"
                )
            }
        }
    }
}