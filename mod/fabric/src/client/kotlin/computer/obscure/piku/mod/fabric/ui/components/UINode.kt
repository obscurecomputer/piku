package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.core.classes.Spacing
import computer.obscure.piku.core.classes.bottomF
import computer.obscure.piku.core.classes.horizontal
import computer.obscure.piku.core.classes.leftF
import computer.obscure.piku.core.classes.rightF
import computer.obscure.piku.core.classes.topF
import computer.obscure.piku.core.classes.vertical
import computer.obscure.piku.mod.fabric.ui.classes.Anchor
import computer.obscure.piku.mod.fabric.ui.classes.Dimension
import computer.obscure.piku.mod.fabric.ui.classes.OffsetDimension
import computer.obscure.piku.mod.fabric.ui.classes.context.LayoutContext
import computer.obscure.piku.mod.fabric.ui.classes.context.MeasureContext
import me.znotchill.kiwi.generated.Color
import net.minecraft.client.gui.GuiGraphics
import java.util.UUID

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

    var onActivate: (() -> Unit)? = null
    var onDeactivate: (() -> Unit)? = null
    var onSelect: (() -> Unit)? = null
    var onDeselect: (() -> Unit)? = null
    var onFocus: (() -> Unit)? = null
    var onUnfocus: (() -> Unit)? = null

    var activated: Boolean = false
    var selected: Boolean = false

    var selectable: Boolean = true

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

    open fun drawSelf(graphics: GuiGraphics, ctx: MeasureContext, parentOpacity: Float = 1f) {
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
        children.forEach { it.drawSelf(graphics, ctx, computedOpacity) }
    }

    protected open fun drawContent(graphics: GuiGraphics, ctx: MeasureContext) {}

    private fun resolveDimension(dim: Dimension, wrapSize: Float, parentSize: Float) = when (val d = dim) {
        Dimension.Wrap -> wrapSize
        Dimension.Fill -> parentSize
        is Dimension.Fixed -> d.px
        is Dimension.Fraction -> parentSize * d.frac
    }
}