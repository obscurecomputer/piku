package computer.obscure.piku.mod.fabric.scripting.old

import computer.obscure.piku.mod.fabric.scripting.old.camera.LuaCinematicCamera
import computer.obscure.piku.mod.fabric.scripting.old.ui.LuaUI
import computer.obscure.twine.annotations.TwineProperty
import computer.obscure.twine.TwineNative

class LuaGame : TwineNative("game") {
    companion object {
        val UI_INSTANCE = LuaUI()
        val CAMERA_INSTANCE = LuaCinematicCamera()
    }
    @TwineProperty
    val ui = UI_INSTANCE

    @TwineProperty
    val camera = CAMERA_INSTANCE
}