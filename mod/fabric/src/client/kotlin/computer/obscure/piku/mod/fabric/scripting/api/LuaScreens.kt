package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.piku.mod.fabric.scripting.api.screen.LuaCustomScreen
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative
import net.minecraft.client.Minecraft

class LuaScreens : TwineNative("screens") {

    @TwineFunction
    fun create(title: String): LuaCustomScreen {
        return LuaCustomScreen(title)
    }

    @TwineFunction
    fun close() {
        Minecraft.getInstance().setScreen(null)
    }
}