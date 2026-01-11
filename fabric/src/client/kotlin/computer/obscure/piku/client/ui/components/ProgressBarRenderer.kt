package computer.obscure.piku.client.ui.components

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import computer.obscure.piku.common.ui.components.ProgressBar

class ProgressBarRenderer : UIComponent<ProgressBar>() {
    override fun drawContent(component: ProgressBar, context: DrawContext, instance: MinecraftClient) {
        val props = component.props

        val x = props.pos.x
        val y = props.pos.y
        val width = props.size.x
        val height = props.size.y

        // background (empty fill)
        val emptyColorAlpha = ((props.fillColor?.a ?: 0) * props.opacity)
            .coerceIn(0f, 255f)
            .toInt()

        props.emptyColor?.let {
            context.fill(
                x.toInt(),
                y.toInt(),
                (x + width).toInt(),
                (y + height).toInt(),
                it.copy(a = emptyColorAlpha).toArgb()
            )
        }

        // progress fill
        val clampedProgress = props.progress.coerceIn(0f, 1f)
        val fillWidth = width * clampedProgress

        val fillColorAlpha = ((props.fillColor?.a ?: 0) * props.opacity)
            .coerceIn(0f, 255f)
            .toInt()

        props.fillColor?.let {
            context.fill(
                x.toInt(),
                y.toInt(),
                (x + fillWidth).toInt(),
                (y + height).toInt(),
                it.copy(a = fillColorAlpha).toArgb()
            )
        }
    }
}
