package computer.obscure.piku.mod.fabric.ui.components

import me.znotchill.kiwi.generated.Vec2
import computer.obscure.piku.mod.fabric.ui.classes.leftF
import computer.obscure.piku.mod.fabric.ui.classes.topF
import computer.obscure.piku.mod.fabric.ui.classes.Dimension
import computer.obscure.piku.mod.fabric.ui.classes.ScaleDimension
import computer.obscure.piku.mod.fabric.ui.classes.context.MeasureContext
import computer.obscure.piku.mod.fabric.ui.text.TextInterpolator
import computer.obscure.piku.mod.fabric.utils.toAdventure
import computer.obscure.piku.mod.fabric.utils.toNativeComponent
import net.kyori.adventure.text.Component
import net.minecraft.network.chat.Component as McComponent
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.util.FormattedCharSequence

open class TextNode() : UINode() {
    var text: Component = Component.empty()
        set(value) {
            if (field == value) return
            field = value
            mcText = value.toNativeComponent()
        }

    var mcText: McComponent = McComponent.empty()
    var rawText: String? = null
    var shadow: Boolean = false
    var scale: Vec2 = Vec2.ONE
    var scaleX: ScaleDimension = ScaleDimension.One
    var scaleY: ScaleDimension = ScaleDimension.One

    var resolvedScaleX: Float = 1f
    var resolvedScaleY: Float = 1f

    private var resolvedText: McComponent = mcText
    private var resolvedLines: List<FormattedCharSequence> = emptyList()
    var wrap: Boolean = true

    constructor(text: Component) : this() {
        this.text = text
    }

    constructor(text: McComponent) : this() {
        this.text = text.toAdventure()
    }

    constructor(text: String) : this(Component.text(text))

    override fun measureContent(ctx: MeasureContext): Pair<Float, Float> {
        resolvedText = TextInterpolator.interpolate(mcText)
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
        drawLines(resolvedLines, graphics, ctx)
    }

    fun drawLine(line: String, graphics: GuiGraphicsExtractor, ctx: MeasureContext) {
        val component = McComponent.literal(line).visualOrderText
        drawLines(listOf(component), graphics, ctx)
    }

    fun drawLines(lines: List<FormattedCharSequence>, graphics: GuiGraphicsExtractor, ctx: MeasureContext) {
        val x = layoutX + padding.leftF
        val y = layoutY + padding.topF
        graphics.pose().pushMatrix()
        graphics.pose().translate(x, y)
        graphics.pose().scale(resolvedScaleX, resolvedScaleY)

        lines.forEachIndexed { index, line ->
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