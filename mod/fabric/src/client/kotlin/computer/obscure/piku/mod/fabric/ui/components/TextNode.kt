package computer.obscure.piku.mod.fabric.ui.components

import me.znotchill.kiwi.generated.Vec2
import computer.obscure.piku.core.classes.leftF
import computer.obscure.piku.core.classes.topF
import computer.obscure.piku.mod.fabric.ui.classes.Dimension
import computer.obscure.piku.mod.fabric.ui.classes.ScaleDimension
import computer.obscure.piku.mod.fabric.ui.classes.context.MeasureContext
import computer.obscure.piku.mod.fabric.ui.text.TextInterpolator
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.network.chat.Component
import net.minecraft.util.FormattedCharSequence

class TextNode(var text: Component) : UINode() {
    var rawText: String? = null
    var shadow: Boolean = false
    var scale: Vec2 = Vec2.ONE
    var scaleX: ScaleDimension = ScaleDimension.One
    var scaleY: ScaleDimension = ScaleDimension.One

    var resolvedScaleX: Float = 1f
    var resolvedScaleY: Float = 1f

    private var resolvedText: Component = text
    private var resolvedLines: List<FormattedCharSequence> = emptyList()
    var wrap: Boolean = true

    constructor(text: String) : this(Component.literal(text))

    override fun measureContent(ctx: MeasureContext): Pair<Float, Float> {
        resolvedText = TextInterpolator.interpolate(text)
        val base = ctx.textRenderer.lineHeight.toFloat()
        resolvedScaleX = scaleX.resolve(ctx.parentScale, ctx.parentWidth, ctx.parentHeight, base).toFloat()
        resolvedScaleY = scaleY.resolve(ctx.parentScale, ctx.parentWidth, ctx.parentHeight, base).toFloat()

        val hasBoundedWidth = width != Dimension.Wrap
        val shouldWrap = wrap && hasBoundedWidth

        if (shouldWrap) {
            val wrapWidthPx = (ctx.parentWidth / resolvedScaleX).toInt().coerceAtLeast(1)
            resolvedLines = ctx.textRenderer.split(resolvedText, wrapWidthPx)
        } else {
            resolvedLines = listOf(resolvedText.visualOrderText)
        }

        val w =
            (resolvedLines.maxOfOrNull {
                ctx.textRenderer.width(it)
            } ?: 0) * resolvedScaleX

        val h = if (resolvedLines.isEmpty()) {
            0f
        } else if (resolvedLines.size == 1) {
            // -1 because the text isn't technically vertically aligned
            // not sure what the issue is, but doing -1 seems to fix the broken
            // vertical alignment,
            // and we want to only apply it to single lines
            (ctx.textRenderer.lineHeight.toFloat() - 1) * resolvedScaleY
        } else {
            (ctx.textRenderer.lineHeight).toFloat() *
                    resolvedLines.size *
                    resolvedScaleY
        }

        return w to h
    }

    override fun drawContent(graphics: GuiGraphicsExtractor, ctx: MeasureContext) {
        val x = layoutX + padding.leftF
        val y = layoutY + padding.topF
        graphics.pose().pushMatrix()
        graphics.pose().translate(x, y)
        graphics.pose().scale(resolvedScaleX, resolvedScaleY)

        resolvedLines.forEachIndexed { index, line ->
            graphics.text(
                ctx.textRenderer,
                line,
                0,
                index * ctx.textRenderer.lineHeight,
                color.withOpacity(computedOpacity).argb,
                shadow
            )
        }

        graphics.pose().popMatrix()
    }
}