package computer.obscure.piku.mod.fabric.ui.menu

import computer.obscure.piku.mod.fabric.ui.components.TextInputNode
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.CommandSuggestions
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.Screen
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

class PikuCommandBox(
    screen: Screen,
    editBox: EditBox,
    textRenderer: Font,
    config: TextInputNode.CommandConfig
) : CommandSuggestions(
    Minecraft.getInstance(),
    screen,
    editBox,
    textRenderer,
    config.commandsOnly, config.onlyShowIfCursorPastError,
    config.lineStartOffset, config.suggestionLineLimit,
    config.anchorToBottom, config.fillColor.argb
)