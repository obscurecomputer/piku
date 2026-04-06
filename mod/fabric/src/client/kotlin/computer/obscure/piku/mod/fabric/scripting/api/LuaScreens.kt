package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.piku.mod.fabric.scripting.api.screen.LuaCustomScreen
import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative
import net.minecraft.client.Minecraft

class LuaScreens : TwineNative("screens") {

    @TwineNativeFunction
    fun create(title: String): LuaCustomScreen {
        return LuaCustomScreen(title)
    }

    @TwineNativeFunction
    fun close() {
        Minecraft.getInstance().setScreen(null)
    }
}