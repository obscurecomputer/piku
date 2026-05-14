package computer.obscure.piku.mod.fabric.ui.components

import computer.obscure.piku.core.classes.Spacing
import computer.obscure.piku.core.graphics.UIColor
import computer.obscure.piku.mod.fabric.ui.Dimension
import computer.obscure.piku.mod.fabric.ui.LayoutContext
import computer.obscure.piku.mod.fabric.ui.MeasureContext
import net.minecraft.client.gui.GuiGraphics

abstract class UINode {

    // STYLE INPUTS
    var width: Dimension = Dimension.Wrap
    var height: Dimension = Dimension.Wrap

    var padding: Spacing = Spacing.ZERO
    var margin: Spacing = Spacing.ZERO

    var background: UIColor? = null

    // COMPUTED LAYOUT
    var layoutX: Float = 0f
    var layoutY: Float = 0f

    var measuredWidth: Float = 0f
    var measuredHeight: Float = 0f

    var visible: Boolean = true

    val children = mutableListOf<UINode>()

    abstract fun measure(ctx: MeasureContext)
    abstract fun layout(ctx: LayoutContext)
    abstract fun draw(ctx: GuiGraphics)
}