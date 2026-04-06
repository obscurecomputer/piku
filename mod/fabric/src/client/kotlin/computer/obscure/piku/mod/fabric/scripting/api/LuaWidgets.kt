package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.piku.core.scripting.api.LuaTextInstance
import computer.obscure.piku.mod.fabric.scripting.api.screen.LuaWidget
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences

class LuaWidgets : TwineNative("widgets") {
    @TwineFunction
    fun button(key: String): LuaWidget {
        return LuaWidget().apply { translationKey = key }
    }

    @TwineFunction
    fun button(luaText: LuaTextInstance): LuaWidget {
        return LuaWidget().apply {
            customLabel = MinecraftClientAudiences.of().asNative(luaText.toComponent())
        }
    }
}