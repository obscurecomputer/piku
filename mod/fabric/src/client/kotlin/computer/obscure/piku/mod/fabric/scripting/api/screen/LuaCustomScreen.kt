package computer.obscure.piku.mod.fabric.scripting.api.screen

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

class LuaCustomScreen(title: String) : TwineNative("") {
    val screen = CustomScreen(Component.literal(title))

    @TwineNativeFunction
    fun grid(columns: Int): LuaGridLayout {
        return LuaGridLayout(columns, screen)
    }

    @TwineNativeFunction
    fun show() {
        Minecraft.getInstance().execute {
            Minecraft.getInstance().setScreen(screen)
        }
    }
}