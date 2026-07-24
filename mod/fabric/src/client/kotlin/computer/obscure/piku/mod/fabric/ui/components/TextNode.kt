package computer.obscure.piku.mod.fabric.ui.components

import me.znotchill.kiwi.generated.Vec2
import computer.obscure.piku.core.classes.leftF
import computer.obscure.piku.core.classes.topF
import computer.obscure.piku.mod.fabric.ui.classes.ScaleDimension
import computer.obscure.piku.mod.fabric.ui.classes.context.MeasureContext
import computer.obscure.piku.mod.fabric.ui.text.TextInterpolator
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.network.chat.Component

class TextNode(var text: Component) : UINode() {
    var shadow: Boolean = false
    var scale: Vec2 = Vec2.ONE

    var scaleX: ScaleDimension = ScaleDimension.One
    var scaleY: ScaleDimension = ScaleDimension.One

    // resolved at measure time, used at draw time
    var resolvedScaleX: Float = 1f
    var resolvedScaleY: Float = 1f

    constructor(text: String) : this(Component.literal(text))

    override fun measureContent(ctx: MeasureContext): Pair<Float, Float> {
        val base = ctx.textRenderer.lineHeight.toFloat()
        resolvedScaleX = scaleX.resolve(ctx.parentScale, ctx.parentWidth, ctx.parentHeight, base).toFloat()
        resolvedScaleY = scaleY.resolve(ctx.parentScale, ctx.parentWidth, ctx.parentHeight, base).toFloat()

        val w = ctx.textRenderer.width(text).toFloat() * resolvedScaleX

        // - 1 because the text isn't technically vertically aligned
        // not sure what the issue is, but doing -1 seems to fix the broken
        // vertical alignment
        val h = (ctx.textRenderer.lineHeight.toFloat() - 1) * resolvedScaleY
        return w to h
    }

    override fun drawContent(graphics: GuiGraphicsExtractor, ctx: MeasureContext) {
        val interpolated = TextInterpolator.interpolate(text)
        val x = layoutX + padding.leftF
        val y = layoutY + padding.topF

        graphics.pose().pushMatrix()
        graphics.pose().translate(x, y)
        graphics.pose().scale(resolvedScaleX, resolvedScaleY)

        graphics.text(
            ctx.textRenderer,
            interpolated,
            0,
            0,
            color.withOpacity(computedOpacity).argb,
            shadow
        )

        graphics.pose().popMatrix()
    }
}