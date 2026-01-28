package computer.obscure.piku.client.ui.components

import computer.obscure.piku.client.ui.UIRenderer
import computer.obscure.piku.common.ui.classes.BorderPosition
import computer.obscure.piku.common.ui.components.Component
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

abstract class UIComponent<T : Component> {
    /**
     * Draw only the inner content of the component (e.g., box fill, gradient, sprite, etc.)
     */
    protected abstract fun drawContent(component: T, context: DrawContext, instance: MinecraftClient)

    /**
     * The final draw call in the process. Applies transforms, renders, and borders.
     */
    fun draw(component: T, context: DrawContext, instance: MinecraftClient) {
        UIRenderer.applyComponentMatrices(context, component)

        drawContent(component, context, instance)
        drawBorder(component, context)

        if (UIRenderer.debugEnabled) {
            drawDebugOverlay(component, context, instance)
        }

        context.matrices.popMatrix()
    }

    private fun drawDebugOverlay(component: T, context: DrawContext, mc: MinecraftClient) {
        val w = component.width().toInt()
        val h = component.height().toInt()
        val color = 0xFFFF00FF.toInt()

        context.fill(0, 0, w, 1, color)
        context.fill(0, h - 1, w, h, color)
        context.fill(0, 0, 1, h, color)
        context.fill(w - 1, 0, w, h, color)

        val label = "${component.name} [${component.compType}]"
        context.matrices.pushMatrix()
        context.matrices.translate(0f, h.toFloat() + 2f)

        val textWidth = mc.textRenderer.getWidth(label)
        context.fill(0, 0, textWidth + 4, 12, 0xAA000000.toInt())
        context.drawText(mc.textRenderer, label, 2, 2, 0xFFFFFFFF.toInt(), false)

        context.matrices.popMatrix()
    }

    /**
     * Default border rendering implementation, shared by all components
     * by default.
     */
    open fun drawBorder(component: Component, context: DrawContext) {
        val border = component.props.border
        if (border.width <= 0f || !component.props.visible) return

        val w = component.width()
        val h = component.height()
        val color = border.color.toArgb()
        val offset = if (border.position == BorderPosition.OUTSIDE) -border.width else 0f

        // top
        context.fill(
            (offset).toInt(),
            (offset).toInt(),
            (w - offset).toInt(),
            (offset + border.width).toInt(),
            color
        )
        // bottom
        context.fill(
            (offset).toInt(),
            (h - offset - border.width).toInt(),
            (w - offset).toInt(),
            (h - offset).toInt(),
            color
        )
        // left
        context.fill(
            (offset).toInt(),
            (offset).toInt(),
            (offset + border.width).toInt(),
            (h - offset).toInt(),
            color
        )
        // right
        context.fill(
            (w - offset - border.width).toInt(),
            (offset).toInt(),
            (w - offset).toInt(),
            (h - offset).toInt(),
            color
        )
    }
}
