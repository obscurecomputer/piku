package computer.obscure.piku.mod.common.ui.components

import computer.obscure.piku.core.ui.classes.FillDirection
import computer.obscure.piku.core.ui.components.ProgressBar
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics

class ProgressBarRenderer : UIComponent<ProgressBar>() {
    override fun drawContent(component: ProgressBar, context: GuiGraphics, instance: Minecraft) {
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

        val fillColorAlpha = ((props.fillColor?.a ?: 0) * props.opacity)
            .coerceIn(0f, 255f)
            .toInt()

        val dir = props.fillDirection ?: FillDirection.RIGHT

        val fillX1: Int
        val fillY1: Int
        val fillX2: Int
        val fillY2: Int

        when (dir) {
            FillDirection.RIGHT -> {
                val fillWidth = width * clampedProgress
                fillX1 = x.toInt()
                fillY1 = y.toInt()
                fillX2 = (x + fillWidth).toInt()
                fillY2 = (y + height).toInt()
            }

            FillDirection.LEFT -> {
                val fillWidth = width * clampedProgress
                fillX1 = (x + width - fillWidth).toInt()
                fillY1 = y.toInt()
                fillX2 = (x + width).toInt()
                fillY2 = (y + height).toInt()
            }

            FillDirection.BOTTOM -> {
                val fillHeight = height * clampedProgress
                fillX1 = x.toInt()
                fillY1 = (y + height - fillHeight).toInt()
                fillX2 = (x + width).toInt()
                fillY2 = (y + height).toInt()
            }

            FillDirection.TOP -> {
                val fillHeight = height * clampedProgress
                fillX1 = x.toInt()
                fillY1 = y.toInt()
                fillX2 = (x + width).toInt()
                fillY2 = (y + fillHeight).toInt()
            }
        }

        props.fillColor?.let {
            context.fill(
                fillX1,
                fillY1,
                fillX2,
                fillY2,
                it.copy(a = fillColorAlpha).toArgb()
            )
        }
    }
}
