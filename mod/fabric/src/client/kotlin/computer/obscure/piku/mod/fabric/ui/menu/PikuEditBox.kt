package computer.obscure.piku.mod.fabric.ui.menu

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.EditBox
import net.minecraft.network.chat.Component

class PikuEditBox(
    textRenderer: Font,
    x: Int, y: Int,
    width: Int, height: Int,
    narration: Component
) : EditBox(textRenderer, x, y, width, height, narration) {
    override fun canConsumeInput(): Boolean = isFocused
    override fun isActive(): Boolean = isFocused
}