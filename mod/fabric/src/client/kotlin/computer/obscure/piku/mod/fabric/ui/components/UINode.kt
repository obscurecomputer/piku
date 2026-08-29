package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.core.classes.Spacing
import computer.obscure.piku.core.classes.bottomF
import computer.obscure.piku.core.classes.horizontal
import computer.obscure.piku.core.classes.leftF
import computer.obscure.piku.core.classes.rightF
import computer.obscure.piku.core.classes.topF
import computer.obscure.piku.core.classes.vertical
import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaUINode
import computer.obscure.piku.mod.fabric.ui.classes.Anchor
import computer.obscure.piku.mod.fabric.ui.classes.Dimension
import computer.obscure.piku.mod.fabric.ui.classes.HitShape
import computer.obscure.piku.mod.fabric.ui.classes.OffsetDimension
import computer.obscure.piku.mod.fabric.ui.classes.ShapeBounds
import computer.obscure.piku.mod.fabric.ui.classes.UIEvent
import computer.obscure.piku.mod.fabric.ui.classes.context.LayoutContext
import computer.obscure.piku.mod.fabric.ui.classes.context.MeasureContext
import me.znotchill.kiwi.generated.Color
import net.minecraft.client.gui.GuiGraphicsExtractor
import java.util.UUID
import kotlin.math.sqrt

abstract class UINode {
    val id: String = UUID.randomUUID().toString()
    var name: String? = null

    var anchor: Anchor = Anchor.TOP_LEFT
    var offsetX: OffsetDimension = OffsetDimension.Zero
    var offsetY: OffsetDimension = OffsetDimension.Zero

    // STYLE INPUTS
    var width: Dimension = Dimension.Wrap
    var height: Dimension = Dimension.Wrap

    var padding: Spacing = Spacing.ZERO
    var margin: Spacing = Spacing.ZERO

    var background: Color? = null
    var color: Color = Color.WHITE

    // COMPUTED LAYOUT
    var layoutX: Float = 0f

    var layoutY: Float = 0f
    var measuredWidth: Float = 0f

    var measuredHeight: Float = 0f
    var visible: Boolean = true
    var opacity: Float = 1f

    var computedOpacity: Float = 1f

    val children = mutableListOf<UINode>()

    // The base hooks for this component.
    // Can not be overriden through Luau!!
    open fun onBaseHover(event: UIEvent, node: LuaUINode) {
        onHover?.invoke(event, node)
    }
    open fun onBaseUnhover(event: UIEvent, node: LuaUINode) {
        onUnhover?.invoke(event, node)
    }
    open fun onBasePress(event: UIEvent, node: LuaUINode) {
        onPress?.invoke(event, node)
    }
    open fun onBaseRelease(event: UIEvent, node: LuaUINode) {
        onRelease?.invoke(event, node)
    }
    open fun onBaseFocus(event: UIEvent, node: LuaUINode) {
        onFocus?.invoke(event, node)
    }
    open fun onBaseUnfocus(event: UIEvent, node: LuaUINode) {
        onUnfocus?.invoke(event, node)
    }

    // The hooks for this component that can be
    // overridden through Luau.
    var onHover: ((UIEvent, LuaUINode) -> Unit)? = null
    var onUnhover: ((UIEvent, LuaUINode) -> Unit)? = null
    var onPress: ((UIEvent, LuaUINode) -> Unit)? = null
    var onRelease: ((UIEvent, LuaUINode) -> Unit)? = null
    var onFocus: ((UIEvent, LuaUINode) -> Unit)? = null
    var onUnfocus: ((UIEvent, LuaUINode) -> Unit)? = null

    var hovered: Boolean = false
    var focused: Boolean = false
    var activated: Boolean = false
    var selected: Boolean = false

    var selectable: Boolean = true

    var hitShape: HitShape = HitShape.Rectangle

    fun containsPoint(x: Float, y: Float): Boolean {
        val bounds = ShapeBounds(layoutX, layoutY, measuredWidth, measuredHeight)
        return hitShape.contains(x, y, bounds)
    }

    protected open fun measureContent(ctx: MeasureContext): Pair<Float, Float> {
        val w = children.maxOfOrNull { it.measuredWidth } ?: 0f
        val h = children.maxOfOrNull { it.measuredHeight } ?: 0f
        return w to h
    }

    fun measureSelf(ctx: MeasureContext) {
        val childCtx = when {
            width != Dimension.Wrap && height != Dimension.Wrap -> ctx.copy(
                parentWidth = (resolveDimension(width, 0f, ctx.parentWidth) - padding.horizontal).coerceAtLeast(0f),
                parentHeight = (resolveDimension(height, 0f, ctx.parentHeight) - padding.vertical).coerceAtLeast(0f)
            )
            width != Dimension.Wrap -> ctx.copy(
                parentWidth = (resolveDimension(width, 0f, ctx.parentWidth) - padding.horizontal).coerceAtLeast(0f)
            )
            height != Dimension.Wrap -> ctx.copy(
                parentHeight = (resolveDimension(height, 0f, ctx.parentHeight) - padding.vertical).coerceAtLeast(0f)
            )
            else -> ctx
        }

        children.forEach { it.measureSelf(childCtx) }
        val (contentW, contentH) = measureContent(childCtx)
        measuredWidth = resolveDimension(width, contentW + padding.horizontal, ctx.parentWidth)
        measuredHeight = resolveDimension(height, contentH + padding.vertical, ctx.parentHeight)
    }

    fun layoutSelf(ctx: LayoutContext) {
        val resolvedX = when (anchor) {
            Anchor.TOP_LEFT, Anchor.CENTER_LEFT, Anchor.BOTTOM_LEFT ->
                ctx.x + margin.leftF
            Anchor.TOP_CENTER, Anchor.CENTER_CENTER, Anchor.BOTTOM_CENTER ->
                ctx.x + (ctx.parentWidth / 2f) - (measuredWidth / 2f)
            Anchor.TOP_RIGHT, Anchor.CENTER_RIGHT, Anchor.BOTTOM_RIGHT ->
                ctx.x + ctx.parentWidth - measuredWidth - margin.rightF
        }

        val resolvedY = when (anchor) {
            Anchor.TOP_LEFT, Anchor.TOP_CENTER, Anchor.TOP_RIGHT ->
                ctx.y + margin.topF
            Anchor.CENTER_LEFT, Anchor.CENTER_CENTER, Anchor.CENTER_RIGHT ->
                ctx.y + (ctx.parentHeight / 2f) - (measuredHeight / 2f)
            Anchor.BOTTOM_LEFT, Anchor.BOTTOM_CENTER, Anchor.BOTTOM_RIGHT ->
                ctx.y + ctx.parentHeight - measuredHeight - margin.bottomF
        }

        layoutX = resolvedX + offsetX.resolve(ctx.parentWidth)
        layoutY = resolvedY + offsetY.resolve(ctx.parentHeight)

        layoutChildren()
    }

    open fun layoutChildren() {
        val childCtx = LayoutContext(
            x = layoutX + padding.leftF,
            y = layoutY + padding.topF,
            parentWidth = measuredWidth - padding.horizontal,
            parentHeight = measuredHeight - padding.vertical
        )
        children.forEach { it.layoutSelf(childCtx) }
    }

    open fun drawSelf(graphics: GuiGraphicsExtractor, ctx: MeasureContext, parentOpacity: Float = 1f) {
        if (!visible || opacity == 0f) return

        computedOpacity = opacity * parentOpacity

        background?.let {
            graphics.fill(
                layoutX.toInt(), layoutY.toInt(),
                (layoutX + measuredWidth).toInt(), (layoutY + measuredHeight).toInt(),
                it.withOpacity(computedOpacity).argb
            )
        }

        drawContent(graphics, ctx)

//        drawDebugOutline(graphics)

        children.forEach { it.drawSelf(graphics, ctx, computedOpacity) }
    }

    private fun drawDebugOutline(graphics: GuiGraphicsExtractor) {
        val bounds = ShapeBounds(layoutX, layoutY, measuredWidth, measuredHeight)
        val points = hitShape.outlinePoints(bounds)
        if (points.isEmpty()) return

        val lineColor = 0xFF00FF00.toInt()
        for (i in points.indices) {
            val (x1, y1) = points[i]
            val (x2, y2) = points[(i + 1) % points.size]
            drawDebugLine(graphics, x1, y1, x2, y2, lineColor)
        }
    }

    private fun drawDebugLine(graphics: GuiGraphicsExtractor, x1: Float, y1: Float, x2: Float, y2: Float, color: Int) {
        // this is REALLY bad
        val dx = x2 - x1
        val dy = y2 - y1
        val length = sqrt(dx * dx + dy * dy)
        if (length < 0.01f) return

        val steps = length.toInt().coerceAtLeast(1)
        for (s in 0..steps) {
            val t = s.toFloat() / steps
            val px = (x1 + dx * t).toInt()
            val py = (y1 + dy * t).toInt()
            graphics.fill(px, py, px + 1, py + 1, color)
        }
    }

    protected open fun drawContent(graphics: GuiGraphicsExtractor, ctx: MeasureContext) {}

    private fun resolveDimension(dim: Dimension, wrapSize: Float, parentSize: Float) = when (val d = dim) {
        Dimension.Wrap -> wrapSize
        Dimension.Fill -> parentSize
        is Dimension.Fixed -> d.px
        is Dimension.Fraction -> parentSize * d.frac
    }
}