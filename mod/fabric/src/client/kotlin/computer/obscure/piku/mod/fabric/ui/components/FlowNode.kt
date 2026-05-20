package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.mod.fabric.ui.classes.Axis
import computer.obscure.piku.mod.fabric.ui.classes.Dimension
import computer.obscure.piku.mod.fabric.ui.classes.alignment.CrossAxisAlignment
import computer.obscure.piku.mod.fabric.ui.classes.alignment.MainAxisAlignment
import computer.obscure.piku.mod.fabric.ui.classes.context.MeasureContext
import net.minecraft.client.gui.GuiGraphics

enum class FlowAxis { HORIZONTAL, VERTICAL }

abstract class FlowNode(var gap: Float = 0f) : UINode() {
    var mainAxis: MainAxisAlignment = MainAxisAlignment.Start
    var crossAxis: CrossAxisAlignment = CrossAxisAlignment.Start
    var scrollable: Boolean = false
    var scrollOffset: Float = 0f
    var clampedScroll: Float = 0f
    var maxScrollExtent: Float = 0f

    abstract val axis: FlowAxis
    private val ax get() =
        if (axis == FlowAxis.HORIZONTAL)
            Axis.Horizontal
        else Axis.Vertical

    private fun totalMainSize(): Float {
        val count = (children.size - 1).coerceAtLeast(0)
        return children
            .sumOf { (ax.mainMeasured(it) + ax.mainMargin(it)).toDouble() }
            .toFloat() + gap * count
    }

    override fun measureContent(ctx: MeasureContext): Pair<Float, Float> {
        val fillChildren = children.filter {
            ax.mainDimension(it) == Dimension.Fill
        }
        val nonFillChildren = children.filter {
            ax.mainDimension(it) != Dimension.Fill
        }

        // fixed children are measured first to know how
        // much space is left for fillChildren
        val fixedMain = nonFillChildren
            .sumOf { (ax.mainMeasured(it) + ax.mainMargin(it)).toDouble() }
            .toFloat() + gap * (children.size - 1).coerceAtLeast(0)

        // divide the remaining space equally across fillChildren
        val remaining = (ax.parentMain(ctx) - fixedMain - ax.mainPadding(padding))
            .coerceAtLeast(0f)

        val fillSize = if (fillChildren.isNotEmpty())
            remaining / fillChildren.size
        else 0f

        // main axis - sum all children widths
        // cross axis - tallest child
        fillChildren.forEach { child ->
            val childMain = (fillSize - ax.mainMargin(child)).coerceAtLeast(0f)
            child.measureSelf(ax.withMain(ctx, childMain))
        }

        val main = children
            .sumOf { (ax.mainMeasured(it) + ax.mainMargin(it)).toDouble() }
            .toFloat() + gap * (children.size - 1).coerceAtLeast(0)
        val cross = children.maxOfOrNull {
            ax.crossMeasured(it) + ax.crossMargin(it)
        } ?: 0f

        return ax.toSize(main, cross)
    }

    override fun layoutChildren() {
        val innerMain = ax.innerMain(this)
        val innerCross = ax.innerCross(this)
        val total = totalMainSize()

        // clamp the scroll
        clampedScroll = if (scrollable) {
            val overflow = (total - innerMain).coerceAtLeast(0f)
            scrollOffset = scrollOffset.coerceIn(-overflow, 0f)
            scrollOffset
        } else 0f

        maxScrollExtent = (total - innerMain).coerceAtLeast(0f)

        // how far to shift the first child from the start edge
        // based on main axis alignment
        val mainOffset = when (mainAxis) {
            MainAxisAlignment.Start -> 0f
            MainAxisAlignment.Center -> (innerMain - total) / 2f
            MainAxisAlignment.End -> innerMain - total
            MainAxisAlignment.SpaceBetween -> 0f
            MainAxisAlignment.SpaceAround -> (innerMain - total) / children.size / 2f
        }

        // extra space inserted when using "SpaceBetween" or
        // "SpaceAround"s
        val spaceBetween = when {
            mainAxis == MainAxisAlignment.SpaceBetween && children.size > 1 ->
                (innerMain - total) / (children.size - 1)
            else -> 0f
        }

        val spaceAround = when (mainAxis) {
            MainAxisAlignment.SpaceAround -> (innerMain - total) / children.size
            else -> 0f
        }

        var cursor = ax.mainStart(this) + clampedScroll + mainOffset

        children.forEach { child ->
            val crossOffset = when (crossAxis) {
                CrossAxisAlignment.Start -> 0f
                CrossAxisAlignment.Center ->
                    (innerCross - ax.crossMeasured(child) - ax.crossMargin(child)) / 2f
                CrossAxisAlignment.End ->
                    innerCross - ax.crossMeasured(child) - ax.crossMargin(child)
            }

            val crossPos = ax.crossStart(this) + ax.crossMarginStart(child) + crossOffset
            val mainPos = cursor + ax.mainMarginStart(child)

            ax.setLayout(child, mainPos, crossPos)
            child.layoutChildren()

            cursor += ax.mainMeasured(child) + ax.mainMargin(child) + gap + spaceBetween + spaceAround
        }
    }

    override fun drawSelf(graphics: GuiGraphics, ctx: MeasureContext, parentOpacity: Float) {
        if (!visible || opacity == 0f) return

        computedOpacity = opacity * parentOpacity
        background?.let {
            graphics.fill(layoutX.toInt(), layoutY.toInt(),
                (layoutX + measuredWidth).toInt(), (layoutY + measuredHeight).toInt(),
                it.withOpacity(computedOpacity).argb)
        }
        if (scrollable) {
            graphics.enableScissor(
                layoutX.toInt(), layoutY.toInt(),
                (layoutX + measuredWidth).toInt(), (layoutY + measuredHeight).toInt()
            )
        }
        drawContent(graphics, ctx)
        children.forEach { child ->
            if (scrollable && !isChildVisible(child)) return@forEach
            child.drawSelf(graphics, ctx, computedOpacity)
        }
        if (scrollable) graphics.disableScissor()
    }

    // cull children that are outside the node's bounds
    private fun isChildVisible(child: UINode): Boolean {
        val nodeBottom = layoutY + measuredHeight
        val nodeRight = layoutX + measuredWidth
        val childBottom = child.layoutY + child.measuredHeight
        val childRight = child.layoutX + child.measuredWidth
        return child.layoutX < nodeRight &&
                childRight > layoutX &&
                child.layoutY < nodeBottom &&
                childBottom > layoutY
    }
}
