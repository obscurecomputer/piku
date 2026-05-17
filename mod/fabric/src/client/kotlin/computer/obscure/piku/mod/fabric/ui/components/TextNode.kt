package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.core.classes.leftF
import computer.obscure.piku.core.classes.topF
import computer.obscure.piku.core.graphics.UIColor
import computer.obscure.piku.mod.fabric.ui.classes.context.MeasureContext
import computer.obscure.piku.mod.fabric.ui.text.TextInterpolator
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

class TextNode(var text: Component) : UINode() {
    var color: UIColor = UIColor.WHITE
    var shadow: Boolean = false

    constructor(text: String) : this(Component.literal(text))

    override fun measureContent(ctx: MeasureContext): Pair<Float, Float> {
        val w = ctx.textRenderer.width(text).toFloat()

        // - 1 because the text isn't technically vertically aligned
        // not sure what the issue is, but doing -1 seems to fix the broken
        // vertical alignment
        val h = ctx.textRenderer.lineHeight.toFloat() - 1
        return w to h
    }

    override fun drawContent(graphics: GuiGraphics, ctx: MeasureContext) {
        val interpolated = TextInterpolator.interpolate(text)
        graphics.drawString(
            ctx.textRenderer,
            interpolated,
            (layoutX + padding.leftF).toInt(),
            (layoutY + padding.topF).toInt(),
            color.argb,
            shadow
        )
    }
}