package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.core.classes.horizontal
import computer.obscure.piku.core.classes.leftF
import computer.obscure.piku.core.classes.topF
import computer.obscure.piku.core.classes.vertical
import computer.obscure.piku.mod.fabric.ui.CrossAxisAlignment
import computer.obscure.piku.mod.fabric.ui.MainAxisAlignment
import computer.obscure.piku.mod.fabric.ui.MeasureContext

class RowNode(var gap: Float = 0f) : UINode() {
    var mainAxis: MainAxisAlignment = MainAxisAlignment.Start
    var crossAxis: CrossAxisAlignment = CrossAxisAlignment.Start

    override fun measureContent(ctx: MeasureContext): Pair<Float, Float> {
        val w = children.sumOf { (it.measuredWidth + it.margin.horizontal).toDouble() }.toFloat() +
                gap * (children.size - 1).coerceAtLeast(0)
        val h = children.maxOfOrNull { it.measuredHeight + it.margin.vertical } ?: 0f
        return w to h
    }

    override fun layoutChildren() {
        val innerW = measuredWidth - padding.horizontal
        val innerH = measuredHeight - padding.vertical
        val totalChildW = children.sumOf { (it.measuredWidth + it.margin.horizontal).toDouble() }.toFloat() +
                gap * (children.size - 1).coerceAtLeast(0)

        var cursor = layoutX + padding.leftF + when (mainAxis) {
            MainAxisAlignment.Start -> 0f
            MainAxisAlignment.Center -> (innerW - totalChildW) / 2f
            MainAxisAlignment.End -> innerW - totalChildW
            MainAxisAlignment.SpaceBetween -> 0f
            MainAxisAlignment.SpaceAround -> (innerW - totalChildW) / children.size / 2f
        }

        val spaceBetween = if (mainAxis == MainAxisAlignment.SpaceBetween && children.size > 1)
            (innerW - totalChildW) / (children.size - 1) else 0f
        val spaceAround = if (mainAxis == MainAxisAlignment.SpaceAround)
            (innerW - totalChildW) / children.size else 0f

        children.forEach { child ->
            val childY = layoutY + padding.topF + child.margin.topF + when (crossAxis) {
                CrossAxisAlignment.Start -> 0f
                CrossAxisAlignment.Center -> (innerH - child.measuredHeight - child.margin.vertical) / 2f
                CrossAxisAlignment.End -> innerH - child.measuredHeight - child.margin.vertical
            }

            child.layoutX = cursor + child.margin.leftF
            child.layoutY = childY
            child.layoutChildren()

            cursor += child.measuredWidth + child.margin.horizontal + gap + spaceBetween + spaceAround
        }
    }
}