package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.core.classes.horizontal
import computer.obscure.piku.core.classes.leftF
import computer.obscure.piku.core.classes.topF
import computer.obscure.piku.core.classes.vertical
import computer.obscure.piku.mod.fabric.ui.CrossAxisAlignment
import computer.obscure.piku.mod.fabric.ui.MainAxisAlignment
import computer.obscure.piku.mod.fabric.ui.MeasureContext
import net.minecraft.client.gui.GuiGraphics

enum class FlowAxis { HORIZONTAL, VERTICAL }

abstract class FlowNode(var gap: Float = 0f) : UINode() {
    var mainAxis: MainAxisAlignment = MainAxisAlignment.Start
    var crossAxis: CrossAxisAlignment = CrossAxisAlignment.Start
    var scrollable: Boolean = false
    var scrollOffset: Float = 0f
    var clampedScroll: Float = 0f

    abstract val axis: FlowAxis

    // total size of all children along the main axis
    private fun totalMainSize(): Float {
        val count = (children.size - 1).coerceAtLeast(0)
        return when (axis) {
            FlowAxis.VERTICAL -> children.sumOf { (it.measuredHeight + it.margin.vertical).toDouble() }.toFloat() + gap * count
            FlowAxis.HORIZONTAL -> children.sumOf { (it.measuredWidth + it.margin.horizontal).toDouble() }.toFloat() + gap * count
        }
    }

    override fun measureContent(ctx: MeasureContext): Pair<Float, Float> {
        return when (axis) {
            FlowAxis.VERTICAL -> {
                val w = children.maxOfOrNull { it.measuredWidth + it.margin.horizontal } ?: 0f
                w to totalMainSize()
            }
            FlowAxis.HORIZONTAL -> {
                val h = children.maxOfOrNull { it.measuredHeight + it.margin.vertical } ?: 0f
                totalMainSize() to h
            }
        }
    }

    override fun layoutChildren() {
        val innerW = measuredWidth - padding.horizontal
        val innerH = measuredHeight - padding.vertical
        val total = totalMainSize()

        clampedScroll = if (scrollable) {
            val overflow = when (axis) {
                FlowAxis.VERTICAL -> (total - innerH).coerceAtLeast(0f)
                FlowAxis.HORIZONTAL -> (total - innerW).coerceAtLeast(0f)
            }
            val clamped = scrollOffset.coerceIn(-overflow, 0f)
            scrollOffset = clamped
            clamped
        } else 0f

        val mainStart = when (axis) {
            FlowAxis.VERTICAL -> layoutY + padding.topF
            FlowAxis.HORIZONTAL -> layoutX + padding.leftF
        }

        val cursor0 = mainStart + clampedScroll + when (mainAxis) {
            MainAxisAlignment.Start -> 0f
            MainAxisAlignment.Center -> when (axis) {
                FlowAxis.VERTICAL -> (innerH - total) / 2f
                FlowAxis.HORIZONTAL -> (innerW - total) / 2f
            }
            MainAxisAlignment.End -> when (axis) {
                FlowAxis.VERTICAL -> innerH - total
                FlowAxis.HORIZONTAL -> innerW - total
            }
            MainAxisAlignment.SpaceBetween -> 0f
            MainAxisAlignment.SpaceAround -> when (axis) {
                FlowAxis.VERTICAL -> (innerH - total) / children.size / 2f
                FlowAxis.HORIZONTAL -> (innerW - total) / children.size / 2f
            }
        }

        val spaceBetween = if (mainAxis == MainAxisAlignment.SpaceBetween && children.size > 1) {
            val inner = if (axis == FlowAxis.VERTICAL) innerH else innerW
            (inner - total) / (children.size - 1)
        } else 0f

        val spaceAround = if (mainAxis == MainAxisAlignment.SpaceAround) {
            val inner = if (axis == FlowAxis.VERTICAL) innerH else innerW
            (inner - total) / children.size
        } else 0f

        var cursor = cursor0

        children.forEach { child ->
            when (axis) {
                FlowAxis.VERTICAL -> {
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
                FlowAxis.HORIZONTAL -> {
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
    }

    override fun drawSelf(graphics: GuiGraphics, ctx: MeasureContext) {
        if (!visible) return
        background?.let {
            graphics.fill(layoutX.toInt(), layoutY.toInt(),
                (layoutX + measuredWidth).toInt(), (layoutY + measuredHeight).toInt(),
                it.argb)
        }
        if (scrollable) {
            graphics.enableScissor(
                layoutX.toInt(), layoutY.toInt(),
                (layoutX + measuredWidth).toInt(), (layoutY + measuredHeight).toInt()
            )
        }
        drawContent(graphics, ctx)
        children.forEach { it.drawSelf(graphics, ctx) }
        if (scrollable) graphics.disableScissor()
    }

    fun onScroll(mouseX: Float, mouseY: Float, delta: Float): Boolean {
        if (!scrollable) return false
        if (mouseX < layoutX || mouseX > layoutX + measuredWidth) return false
        if (mouseY < layoutY || mouseY > layoutY + measuredHeight) return false
        scrollOffset += delta * 12f
        return true
    }
}
