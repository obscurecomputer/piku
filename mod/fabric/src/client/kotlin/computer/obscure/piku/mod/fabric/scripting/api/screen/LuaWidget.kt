package computer.obscure.piku.mod.fabric.scripting.api.screen

import computer.obscure.twine.LuaCallback
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.TwineNative
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component

class LuaWidget : TwineNative("") {
    var spanColumns = 1
    var buttonWidth = 98
    var translationKey = ""
    var literalText = ""
    var customLabel: Component? = null
    var clickHandler: (() -> Unit)? = null

    @TwineFunction
    fun width(w: Int): LuaWidget { buttonWidth = w; return this }

    @TwineFunction
    fun span(cols: Int): LuaWidget { spanColumns = cols; return this }

    @TwineFunction
    fun onClick(fn: LuaCallback): LuaWidget {
        clickHandler = { fn.invoke() }
        return this
    }

    fun build(): Button {
        val label = customLabel
            ?: if (literalText.isNotEmpty()) Component.literal(literalText)
            else Component.translatable(translationKey)

        return Button.builder(label) {
            clickHandler?.invoke()
        }.width(buttonWidth).build()
    }
}