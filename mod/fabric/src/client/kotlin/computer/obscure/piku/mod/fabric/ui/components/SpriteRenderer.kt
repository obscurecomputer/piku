package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.core.ui.components.Sprite
import computer.obscure.piku.mod.fabric.ui.UIRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderPipelines

class SpriteRenderer : UIComponent<Sprite>() {
    override fun drawContent(component: Sprite, context: GuiGraphics, instance: Minecraft) {
        val props = component.props
        val texture = UIRenderer.getIdentifier(props.texturePath) ?: return
        val texWidth = component.computedSize?.x?.toInt() ?: 16
        val texHeight = component.computedSize?.y?.toInt() ?: 16

        var drawWidth = props.size.x.toInt().takeIf { it > 0 } ?: texWidth
        var drawHeight = props.size.y.toInt().takeIf { it > 0 } ?: texHeight

        if (props.fillScreen) {
            val window = instance.window
            drawWidth = window.guiScaledWidth
            drawHeight = window.guiScaledHeight
        }

        val unscaledCompHeight = component.height() / props.scale.y
        val yOffset = (unscaledCompHeight - drawHeight) / 2.0
        val finalAlpha = (props.color.a * props.opacity)
            .coerceIn(0f, 255f)
            .toInt()

        val colorArgb = props.color.copy(a = finalAlpha).toArgb()

        context.blit(
            RenderPipelines.GUI_TEXTURED,
            texture,
            0,
            yOffset.toInt(),
            0f, 0f,
            drawWidth, drawHeight,
            drawWidth, drawHeight,
            colorArgb
        )
    }
}
