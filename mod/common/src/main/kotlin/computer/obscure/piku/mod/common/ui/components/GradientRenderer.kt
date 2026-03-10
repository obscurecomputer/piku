package computer.obscure.piku.mod.common.ui.components

import computer.obscure.piku.core.ui.components.Gradient
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics

class GradientRenderer : UIComponent<Gradient>() {
    override fun drawContent(component: Gradient, context: GuiGraphics, instance: Minecraft) {
        val props = component.props
        var width = props.size.x.toInt()
        var height = props.size.y.toInt()

        if (props.fillScreen) {
            val window = instance.window
            width = window.guiScaledWidth
            height = window.guiScaledHeight
        }

        context.fillGradient(0, 0, width, height, props.from.toArgb(), props.to.toArgb())
    }
}