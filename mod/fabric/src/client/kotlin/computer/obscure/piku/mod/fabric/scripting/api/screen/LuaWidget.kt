package computer.obscure.piku.mod.fabric.scripting.api.screen

import computer.obscure.twine.annotations.TwineNativeFunction
import computer.obscure.twine.nativex.TwineNative
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import org.luaj.vm2.LuaFunction

class LuaWidget : TwineNative("") {
    var spanColumns = 1
    var buttonWidth = 98
    var translationKey = ""
    var literalText = ""
    var customLabel: Component? = null
    var clickHandler: LuaFunction? = null

    @TwineNativeFunction
    fun width(w: Int): LuaWidget { buttonWidth = w; return this }

    @TwineNativeFunction
    fun span(cols: Int): LuaWidget { spanColumns = cols; return this }

    @TwineNativeFunction
    fun onClick(fn: LuaFunction): LuaWidget { clickHandler = fn; return this }

    fun build(): Button {
        val label = customLabel
            ?: if (literalText.isNotEmpty()) Component.literal(literalText)
            else Component.translatable(translationKey)

        return Button.builder(label) {
            clickHandler?.call()
        }.width(buttonWidth).build()
    }
}