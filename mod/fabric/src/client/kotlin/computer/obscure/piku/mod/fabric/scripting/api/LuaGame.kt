package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.piku.mod.fabric.scripting.api.camera.LuaCamera
import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaUI
import computer.obscure.twine.annotations.TwineProperty
import computer.obscure.twine.TwineNative

class LuaGame : TwineNative("game") {
    companion object {
        val UI_INSTANCE = LuaUI()
        val CAMERA_INSTANCE = LuaCamera()
    }
    @TwineProperty
    val ui = UI_INSTANCE

    @TwineProperty
    val camera = CAMERA_INSTANCE
}