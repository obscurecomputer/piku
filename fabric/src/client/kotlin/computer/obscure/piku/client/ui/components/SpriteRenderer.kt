package computer.obscure.piku.client.ui.components

import computer.obscure.piku.client.ui.UIRenderer
import computer.obscure.piku.common.ui.components.Sprite
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.math.ColorHelper

class SpriteRenderer : UIComponent<Sprite>() {
    override fun drawContent(component: Sprite, context: DrawContext, instance: MinecraftClient) {
        val props = component.props
        val texture = UIRenderer.getIdentifier(props.texturePath)
        val texWidth = component.computedSize?.x?.toInt() ?: 16
        val texHeight = component.computedSize?.y?.toInt() ?: 16

        var drawWidth = props.size.x.toInt().takeIf { it > 0 } ?: texWidth
        var drawHeight = props.size.y.toInt().takeIf { it > 0 } ?: texHeight

        if (props.fillScreen) {
            val window = instance.window
            drawWidth = window.scaledWidth
            drawHeight = window.scaledHeight
        }

        val color = ColorHelper.withAlpha(props.opacity, -1)

        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            texture,
            0,
            0,
            0f, 0f,
            drawWidth, drawHeight,
            drawWidth, drawHeight,
            color
        )
    }
}
