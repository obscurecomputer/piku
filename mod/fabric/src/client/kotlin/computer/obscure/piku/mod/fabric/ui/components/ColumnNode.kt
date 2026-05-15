package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.core.classes.horizontal
import computer.obscure.piku.core.classes.leftF
import computer.obscure.piku.core.classes.topF
import computer.obscure.piku.core.classes.vertical
import computer.obscure.piku.mod.fabric.ui.CrossAxisAlignment
import computer.obscure.piku.mod.fabric.ui.MainAxisAlignment
import computer.obscure.piku.mod.fabric.ui.MeasureContext

class ColumnNode(var gap: Float = 0f) : UINode() {
    var mainAxis: MainAxisAlignment = MainAxisAlignment.Start
    var crossAxis: CrossAxisAlignment = CrossAxisAlignment.Start

    override fun measureContent(ctx: MeasureContext): Pair<Float, Float> {
        val w = children.maxOfOrNull { it.measuredWidth + it.margin.horizontal } ?: 0f
        val h = children.sumOf { (it.measuredHeight + it.margin.vertical).toDouble() }.toFloat() +
                gap * (children.size - 1).coerceAtLeast(0)
        return w to h
    }

    override fun layoutChildren() {
        val innerW = measuredWidth - padding.horizontal
        val innerH = measuredHeight - padding.vertical
        val totalChildH = children.sumOf { (it.measuredHeight + it.margin.vertical).toDouble() }.toFloat() +
                gap * (children.size - 1).coerceAtLeast(0)

        var cursor = layoutY + padding.topF + when (mainAxis) {
            MainAxisAlignment.Start -> 0f
            MainAxisAlignment.Center -> (innerH - totalChildH) / 2f
            MainAxisAlignment.End -> innerH - totalChildH
            MainAxisAlignment.SpaceBetween -> 0f
            MainAxisAlignment.SpaceAround -> (innerH - totalChildH) / children.size / 2f
        }

        val spaceBetween = if (mainAxis == MainAxisAlignment.SpaceBetween && children.size > 1)
            (innerH - totalChildH) / (children.size - 1) else 0f
        val spaceAround = if (mainAxis == MainAxisAlignment.SpaceAround)
            (innerH - totalChildH) / children.size else 0f

        children.forEach { child ->
            val childX = layoutX + padding.leftF + child.margin.leftF + when (crossAxis) {
                CrossAxisAlignment.Start -> 0f
                CrossAxisAlignment.Center -> (innerW - child.measuredWidth - child.margin.horizontal) / 2f
                CrossAxisAlignment.End -> innerW - child.measuredWidth - child.margin.horizontal
            }

            child.layoutX = childX
            child.layoutY = cursor + child.margin.topF
            child.layoutChildren()

            cursor += child.measuredHeight + child.margin.vertical + gap + spaceBetween + spaceAround
        }
    }
}