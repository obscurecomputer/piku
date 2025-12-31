package me.znotchill.piku.client.scripting.api

import dev.znci.twine.annotations.TwineNativeProperty
import dev.znci.twine.nativex.TwineNative
import me.znotchill.piku.client.scripting.api.ui.LuaUI

class LuaGame : TwineNative("game") {
    @TwineNativeProperty
    val ui: LuaUI = LuaUI()
}