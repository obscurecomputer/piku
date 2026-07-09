package computer.obscure.piku.mod.fabric.scripting.api.controlify

import computer.obscure.piku.core.scripting.api.LuaText
import computer.obscure.piku.core.scripting.api.LuaTextInstance
import computer.obscure.piku.mod.fabric.controlify.ControlifyIntegration
import computer.obscure.piku.mod.fabric.utils.toAdventure
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction

class LuaControlify : TwineNative("controlify") {
    @TwineFunction
    fun glyph(value: String): LuaTextInstance {
        val glyph = ControlifyIntegration.getGlyph(value)
            ?: return LuaTextInstance("text", "NO_GLYPH")

        return LuaText.fromComponent(glyph.toAdventure())
    }
}