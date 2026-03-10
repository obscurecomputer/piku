package computer.obscure.piku.mod.common.ui.components

import computer.obscure.piku.core.ui.components.FlowContainer
import computer.obscure.piku.mod.common.ui.draw
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics

class FlowContainerRenderer : UIComponent<FlowContainer>() {
    override fun drawContent(component: FlowContainer, context: GuiGraphics, instance: Minecraft) {
        val props = component.props

        props.backgroundColor?.let { bg ->
            val components = props.components
            val minX = components.minOfOrNull { it.screenX } ?: 0
            val maxX = components.maxOfOrNull { it.screenX + it.width() } ?: 0
            val minY = components.minOfOrNull { it.screenY } ?: 0
            val maxY = components.maxOfOrNull { it.screenY + it.height() } ?: 0

            context.fill(
                (minX - props.padding.left).toInt(),
                (minY - props.padding.top).toInt(),
                maxX.toInt() + props.padding.right.toInt(),
                maxY.toInt() + props.padding.bottom.toInt(),
                bg.toArgb()
            )
        }

        props.components.forEach { child ->
            child.draw(context)
        }
    }
}
