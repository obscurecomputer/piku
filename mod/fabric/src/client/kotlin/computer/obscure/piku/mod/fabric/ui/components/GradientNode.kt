package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.core.graphics.UIColor
import computer.obscure.piku.mod.fabric.ui.MeasureContext
import net.minecraft.client.gui.GuiGraphics

class GradientNode(
    var colorStart: UIColor = UIColor.BLACK,
    var colorEnd: UIColor = UIColor.WHITE,
    var direction: GradientDirection = GradientDirection.HORIZONTAL
) : UINode() {

    enum class GradientDirection { HORIZONTAL, VERTICAL }

    override fun measureContent(ctx: MeasureContext) = 0f to 0f // purely sized by width/height

    override fun drawContent(graphics: GuiGraphics, ctx: MeasureContext) {
        val x1 = layoutX.toInt()
        val y1 = layoutY.toInt()
        val x2 = (layoutX + measuredWidth).toInt()
        val y2 = (layoutY + measuredHeight).toInt()

        when (direction) {
            GradientDirection.HORIZONTAL ->
                graphics.fillGradient(x1, y1, x2, y2, colorStart.argb, colorEnd.argb)
            GradientDirection.VERTICAL -> {
                // TODO: find a vertical implementation of this?
                // could not find one?
            }
        }
    }
}