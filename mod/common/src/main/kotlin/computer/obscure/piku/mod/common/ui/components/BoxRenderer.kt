package computer.obscure.piku.mod.common.ui.components

import computer.obscure.piku.core.ui.components.Box
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics

class BoxRenderer : UIComponent<Box>() {
    override fun drawContent(component: Box, context: GuiGraphics, instance: Minecraft) {
        val props = component.props
        var drawWidth = props.size.x.toInt()
        var drawHeight = props.size.y.toInt()

        if (props.fillScreen) {
            val window = instance.window
            drawWidth = window.guiScaledWidth
            drawHeight = window.guiScaledHeight
        }

        val alpha = (props.color.a * props.opacity)
            .coerceIn(0f, 255f)
            .toInt()

        context.fill(
            0, 0,
            drawWidth, drawHeight,
            props.color.copy(a = alpha).toArgb()
        )
    }
}