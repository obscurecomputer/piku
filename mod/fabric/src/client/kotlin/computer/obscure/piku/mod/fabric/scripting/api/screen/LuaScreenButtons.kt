package computer.obscure.piku.mod.fabric.scripting.api.screen

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.contents.TranslatableContents

class LuaScreenButtons(val screen: Screen) : TwineNative() {

    @TwineNativeFunction
    fun has(key: String): Boolean {
        return screen.children().any { listener ->
            listener is Button &&
            listener.message.contents is TranslatableContents &&
            (listener.message.contents as TranslatableContents).key == key
        }
    }

    @TwineNativeFunction
    fun keys(): List<String> {
        return screen.children().filterIsInstance<Button>()
            .mapNotNull { button ->
                (button.message.contents as? TranslatableContents)?.key
            }
    }
}