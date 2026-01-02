package me.znotchill.piku.client.scripting.api

import computer.obscure.twine.annotations.TwineNativeProperty
import computer.obscure.twine.nativex.TwineNative
import me.znotchill.piku.client.scripting.api.ui.LuaUI

class LuaGame : TwineNative("game") {
    @TwineNativeProperty
    val ui: LuaUI = LuaUI()
}