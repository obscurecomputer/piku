package computer.obscure.piku.client.ui.components

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import computer.obscure.piku.client.ui.draw
import computer.obscure.piku.common.ui.components.Group

class GroupRenderer : UIComponent<Group>() {
    override fun drawContent(component: Group, context: DrawContext, instance: MinecraftClient) {
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
