package computer.obscure.piku.mod.common.scripting.api

import computer.obscure.piku.mod.common.scripting.api.camera.LuaCamera
import computer.obscure.piku.mod.common.scripting.api.ui.LuaUI
import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative

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