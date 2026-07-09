package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.core.classes.Vec2
import computer.obscure.piku.mod.fabric.ui.classes.context.MeasureContext
import net.minecraft.client.gui.GuiGraphics
import kotlin.math.abs

class LineNode(
    var to: Vec2 = Vec2.ZERO,
    var thickness: Int = 2,
    var fixedOrigin: Boolean = false,
) : UINode() {

    override fun measureContent(ctx: MeasureContext): Pair<Float, Float> {
        return if (fixedOrigin) 0f to 0f
        else abs(to.x).toFloat() to abs(to.y).toFloat()
    }

    // TODO: Optimise, this is AWFUL
    override fun drawContent(graphics: GuiGraphics, ctx: MeasureContext) {
        val origin = Vec2(layoutX.toDouble(), layoutY.toDouble())
        val target = Vec2(layoutX.toDouble() + to.x, layoutY.toDouble() + to.y)
        val points = origin.lineTo(target)
        val half = thickness / 2
        for (p in points) {
            val px = p.x.toInt()
            val py = p.y.toInt()
            graphics.fill(
                px - half, py - half,
                px - half + thickness, py - half + thickness,
                color.withOpacity(computedOpacity).argb
            )
        }
    }
}