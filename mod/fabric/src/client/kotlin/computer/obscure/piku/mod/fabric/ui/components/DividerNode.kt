package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.core.graphics.UIColor
import computer.obscure.piku.mod.fabric.ui.classes.context.MeasureContext
import net.minecraft.client.gui.GuiGraphics

class DividerNode(var color: UIColor = UIColor.hex("#44ffffff")) : UINode() {
    var thickness: Float = 1f

    override fun measureContent(ctx: MeasureContext) = 0f to thickness

    override fun drawContent(graphics: GuiGraphics, ctx: MeasureContext) {
        graphics.fill(
            layoutX.toInt(), layoutY.toInt(),
            (layoutX + measuredWidth).toInt(), (layoutY + thickness).toInt(),
            color.argb
        )
    }
}