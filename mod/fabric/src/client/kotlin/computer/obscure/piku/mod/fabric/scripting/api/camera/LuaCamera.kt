package computer.obscure.piku.mod.fabric.scripting.api.camera

import computer.obscure.piku.core.scripting.api.LuaVec3Instance
import computer.obscure.piku.core.scripting.engine.EngineError
import computer.obscure.piku.core.scripting.engine.EngineErrorCode
import computer.obscure.piku.mod.fabric.camera.CinematicCamera
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative
import net.minecraft.world.phys.Vec3

class LuaCamera : TwineNative() {
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
    fun move(to: LuaVec3Instance) {
        CinematicCamera.pos = Vec3(to.x, to.y, to.z)
    }

    @TwineFunction
    fun rotate(to: LuaVec3Instance) {
        CinematicCamera.rotation = Vec3(to.x, to.y, to.z)
    }

    @TwineFunction
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