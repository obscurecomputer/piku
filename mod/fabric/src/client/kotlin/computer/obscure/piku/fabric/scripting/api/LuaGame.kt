package computer.obscure.piku.fabric.scripting.api

import computer.obscure.piku.fabric.scripting.api.camera.LuaCamera
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative
import computer.obscure.piku.fabric.scripting.api.ui.LuaUI

class LuaGame : TwineNative("game") {
    companion object {
        val UI_INSTANCE = LuaUI()
        val CAMERA_INSTANCE = LuaCamera()
    }
    @TwineNativeProperty
    val ui = UI_INSTANCE

    @TwineNativeProperty
    val camera = CAMERA_INSTANCE
}