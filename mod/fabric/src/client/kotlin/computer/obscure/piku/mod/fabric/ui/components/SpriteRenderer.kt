package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.core.ui.components.Sprite
import computer.obscure.piku.mod.fabric.ui.UIRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderPipelines

class SpriteRenderer : UIComponent<Sprite>() {
    override fun drawContent(component: Sprite, context: GuiGraphics, instance: Minecraft) {
        val props = component.props

        if (props.backgroundColor.a > 0) {
            val bgColor = props.backgroundColor.copy(a = (props.backgroundColor.a * props.opacity).toInt()).toArgb()
            context.fill(0, 0, component.width().toInt(), component.height().toInt(), bgColor)
        }

        val texture = UIRenderer.getIdentifier(props.texturePath) ?: return

        val texWidth = component.computedSize?.x?.toInt() ?: 16
        val texHeight = component.computedSize?.y?.toInt() ?: 16
        val containerWidth = component.width().toInt()
        val containerHeight = component.height().toInt()

        val finalAlpha = (props.color.a * props.opacity).coerceIn(0f, 255f).toInt()
        val colorArgb = props.color.copy(a = finalAlpha).toArgb()
        if (props.fillScreen) {
            context.pose().pushMatrix()
            context.pose().identity()

            val drawWidth = instance.window.guiScaledWidth
            val drawHeight = instance.window.guiScaledHeight

            context.blit(
                RenderPipelines.GUI_TEXTURED,
                texture,
                0, 0,
                0f, 0f,
                drawWidth, drawHeight,
                drawWidth, drawHeight,
                colorArgb
            )

            context.pose().popMatrix()
        } else if (props.tiled) {
            for (x in 0 until containerWidth step texWidth) {
                for (y in 0 until containerHeight step texHeight) {
                    val drawW = minOf(texWidth, containerWidth - x)
                    val drawH = minOf(texHeight, containerHeight - y)

                    context.blit(
                        RenderPipelines.GUI_TEXTURED,
                        texture,
                        x, y,
                        0f, 0f,
                        drawW, drawH,
                        texWidth, texHeight,
                        colorArgb
                    )
                }
            }
        } else {
            var drawWidth = props.size.x.toInt().takeIf { it > 0 } ?: texWidth
            var drawHeight = props.size.y.toInt().takeIf { it > 0 } ?: texHeight

            if (props.fillScreen) {
                drawWidth = instance.window.guiScaledWidth
                drawHeight = instance.window.guiScaledHeight
            }

            context.blit(
                RenderPipelines.GUI_TEXTURED,
                texture,
                0, 0,
                0f, 0f,
                drawWidth, drawHeight,
                drawWidth, drawHeight,
                colorArgb
            )
        }
    }
}