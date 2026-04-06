package computer.obscure.piku.mod.fabric.scripting.api.screen

import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

class LuaCustomScreen(title: String) : TwineNative("") {
    val screen = CustomScreen(Component.literal(title))

    @TwineFunction
    fun grid(columns: Int): LuaGridLayout {
        return LuaGridLayout(columns, screen)
    }

    @TwineFunction
    fun show() {
        Minecraft.getInstance().schedule {
            Minecraft.getInstance().setScreen(screen)
        }
    }
}