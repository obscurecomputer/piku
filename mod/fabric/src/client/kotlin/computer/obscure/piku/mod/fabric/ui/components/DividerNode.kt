package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.mod.fabric.ui.classes.context.MeasureContext
import net.minecraft.client.gui.GuiGraphicsExtractor

class DividerNode : UINode() {
    var thickness: Float = 1f

    override fun measureContent(ctx: MeasureContext) = 0f to thickness

    override fun drawContent(graphics: GuiGraphicsExtractor, ctx: MeasureContext) {
        graphics.fill(
            layoutX.toInt(), layoutY.toInt(),
            (layoutX + measuredWidth).toInt(), (layoutY + thickness).toInt(),
            color.argb
        )
    }
}