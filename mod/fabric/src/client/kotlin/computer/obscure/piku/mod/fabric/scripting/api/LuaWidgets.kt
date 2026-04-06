package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.piku.core.scripting.api.LuaTextInstance
import computer.obscure.piku.mod.fabric.scripting.api.screen.LuaWidget
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.annotations.TwineOverload
import computer.obscure.twine.nativex.TwineNative
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences

class LuaWidgets : TwineNative("widgets") {
    @TwineOverload
    @TwineNativeFunction
    fun button(key: String): LuaWidget {
        return LuaWidget().apply { translationKey = key }
    }

    @TwineOverload
    @TwineNativeFunction
    fun button(luaText: LuaTextInstance): LuaWidget {
        return LuaWidget().apply {
            customLabel = MinecraftClientAudiences.of().asNative(luaText.toComponent())
        }
    }
}