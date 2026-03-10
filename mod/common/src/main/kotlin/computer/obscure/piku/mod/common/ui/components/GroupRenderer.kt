package computer.obscure.piku.mod.common.ui.components

import computer.obscure.piku.core.ui.components.Group
import computer.obscure.piku.mod.common.ui.draw
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics

class GroupRenderer : UIComponent<Group>() {
    override fun drawContent(component: Group, context: GuiGraphics, instance: Minecraft) {
        val props = component.props

        props.backgroundColor?.let { bg ->
            val w = component.width()
            val h = component.height()

            context.fill(
                0,
                0,
                w.toInt(),
                h.toInt(),
                bg.toArgb()
            )
        }

        props.components.forEach { it.draw(context) }
    }
}
