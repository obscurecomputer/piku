package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.mod.fabric.ui.classes.context.MeasureContext
import me.znotchill.kiwi.generated.Color
import net.minecraft.client.gui.GuiGraphics

class ProgressBarNode(
    var value: Float = 0f, // 0.0 to 1.0
    var foreground: Color = Color.GREEN,
    var track: Color = Color.hex("#44ffffff")
) : UINode() {

    override fun measureContent(ctx: MeasureContext) = 0f to 4f

    override fun drawContent(graphics: GuiGraphics, ctx: MeasureContext) {
        val x = layoutX.toInt()
        val y = layoutY.toInt()
        val w = measuredWidth.toInt()
        val h = measuredHeight.toInt()

        graphics.fill(
            x, y,
            x + w, y + h,
            track.withOpacity(computedOpacity).argb
        )
        graphics.fill(
            x, y,
            x + (w * value.coerceIn(0f, 1f)).toInt(),
            y + h,
            foreground.withOpacity(computedOpacity).argb
        )
    }
}