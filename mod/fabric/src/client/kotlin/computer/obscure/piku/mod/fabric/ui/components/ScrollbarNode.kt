package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.core.graphics.UIColor
import computer.obscure.piku.mod.fabric.ui.classes.context.MeasureContext
import net.minecraft.client.gui.GuiGraphics

enum class ScrollbarDirection { VERTICAL, HORIZONTAL }

class ScrollbarNode(var target: FlowNode? = null) : UINode() {
    var trackColor: UIColor = UIColor.hex("#44ffffff")
    var thumbColor: UIColor = UIColor.hex("#88ffffff")
    var direction: ScrollbarDirection = ScrollbarDirection.VERTICAL

    override fun measureContent(ctx: MeasureContext) = when (direction) {
        ScrollbarDirection.VERTICAL -> 4f to 0f
        ScrollbarDirection.HORIZONTAL -> 0f to 4f
    }

    override fun drawContent(graphics: GuiGraphics, ctx: MeasureContext) {
        val t = target ?: return
        val x = layoutX.toInt()
        val y = layoutY.toInt()
        val w = measuredWidth.toInt()
        val h = measuredHeight.toInt()

        graphics.fill(x, y, x + w, y + h, trackColor.withOpacity(computedOpacity).argb)

        val maxScroll = t.maxScrollExtent
        if (maxScroll <= 0f) return

        val scrollFraction = (-t.clampedScroll) / maxScroll.coerceAtLeast(1f)

        when (direction) {
            ScrollbarDirection.VERTICAL -> {
                val total = maxScroll + h
                val thumbH = ((h.toFloat() / total) * h).coerceAtLeast(16f).toInt()
                val thumbY = y + ((h - thumbH) * scrollFraction).toInt()
                graphics.fill(x, thumbY, x + w, thumbY + thumbH, thumbColor.withOpacity(computedOpacity).argb)
            }
            ScrollbarDirection.HORIZONTAL -> {
                val total = maxScroll + w
                val thumbW = ((w.toFloat() / total) * w).coerceAtLeast(16f).toInt()
                val thumbX = x + ((w - thumbW) * scrollFraction).toInt()
                graphics.fill(thumbX, y, thumbX + thumbW, y + h, thumbColor.withOpacity(computedOpacity).argb)
            }
        }
    }
}