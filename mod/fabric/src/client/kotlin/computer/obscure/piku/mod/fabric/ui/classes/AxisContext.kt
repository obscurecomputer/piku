package computer.obscure.piku.mod.fabric.ui.classes

import computer.obscure.piku.core.classes.Spacing
import computer.obscure.piku.core.classes.horizontal
import computer.obscure.piku.core.classes.leftF
import computer.obscure.piku.core.classes.topF
import computer.obscure.piku.core.classes.vertical
import computer.obscure.piku.mod.fabric.ui.classes.context.MeasureContext
import computer.obscure.piku.mod.fabric.ui.components.FlowNode
import computer.obscure.piku.mod.fabric.ui.components.UINode

sealed interface Axis {
    fun mainMeasured(node: UINode): Float
    fun crossMeasured(node: UINode): Float
    fun mainMargin(node: UINode): Float
    fun crossMargin(node: UINode): Float
    fun mainDimension(node: UINode): Dimension
    fun parentMain(ctx: MeasureContext): Float
    fun mainPadding(padding: Spacing): Float
    fun withMain(ctx: MeasureContext, value: Float): MeasureContext
    fun toSize(main: Float, cross: Float): Pair<Float, Float>

    fun mainMarginStart(node: UINode): Float
    fun crossMarginStart(node: UINode): Float
    fun innerMain(node: FlowNode): Float
    fun innerCross(node: FlowNode): Float
    fun mainStart(node: FlowNode): Float
    fun crossStart(node: FlowNode): Float
    fun setLayout(child: UINode, mainPos: Float, crossPos: Float)

    fun parentCross(ctx: MeasureContext): Float
    fun crossPadding(padding: Spacing): Float
    fun withCross(ctx: MeasureContext, value: Float): MeasureContext

    object Horizontal : Axis {
        override fun mainMeasured(node: UINode) =
            node.measuredWidth

        override fun crossMeasured(node: UINode) =
            node.measuredHeight

        override fun mainMargin(node: UINode) =
            node.margin.horizontal

        override fun crossMargin(node: UINode) =
            node.margin.vertical

        override fun mainDimension(node: UINode) =
            node.width

        override fun parentMain(ctx: MeasureContext) =
            ctx.parentWidth

        override fun mainPadding(padding: Spacing) =
            padding.horizontal

        override fun withMain(ctx: MeasureContext, value: Float) =
            ctx.copy(parentWidth = value)

        override fun toSize(main: Float, cross: Float) =
            main to cross

        override fun mainMarginStart(node: UINode) =
            node.margin.leftF

        override fun crossMarginStart(node: UINode) =
            node.margin.topF

        override fun innerMain(node: FlowNode) =
            node.measuredWidth - node.padding.horizontal

        override fun innerCross(node: FlowNode) =
            node.measuredHeight - node.padding.vertical

        override fun mainStart(node: FlowNode) =
            node.layoutX + node.padding.leftF

        override fun crossStart(node: FlowNode) =
            node.layoutY + node.padding.topF

        override fun setLayout(child: UINode, mainPos: Float, crossPos: Float) {
            child.layoutX = mainPos
            child.layoutY = crossPos
        }

        override fun parentCross(ctx: MeasureContext) =
            ctx.parentHeight
        override fun crossPadding(padding: Spacing) =
            padding.vertical
        override fun withCross(ctx: MeasureContext, value: Float) =
            ctx.copy(parentHeight = value)
    }

    object Vertical : Axis {
        override fun mainMeasured(node: UINode) =
            node.measuredHeight

        override fun crossMeasured(node: UINode) =
            node.measuredWidth

        override fun mainMargin(node: UINode) =
            node.margin.vertical

        override fun crossMargin(node: UINode) =
            node.margin.horizontal

        override fun mainDimension(node: UINode) =
            node.height

        override fun parentMain(ctx: MeasureContext) =
            ctx.parentHeight

        override fun mainPadding(padding: Spacing) =
            padding.vertical

        override fun withMain(ctx: MeasureContext, value: Float) =
            ctx.copy(parentHeight = value)

        override fun toSize(main: Float, cross: Float) =
            cross to main

        override fun mainMarginStart(node: UINode) =
            node.margin.topF

        override fun crossMarginStart(node: UINode) =
            node.margin.leftF

        override fun innerMain(node: FlowNode) =
            node.measuredHeight - node.padding.vertical

        override fun innerCross(node: FlowNode) =
            node.measuredWidth - node.padding.horizontal

        override fun mainStart(node: FlowNode) =
            node.layoutY + node.padding.topF

        override fun crossStart(node: FlowNode) =
            node.layoutX + node.padding.leftF

        override fun setLayout(child: UINode, mainPos: Float, crossPos: Float) {
            child.layoutX = crossPos
            child.layoutY = mainPos
        }

        override fun parentCross(ctx: MeasureContext) =
            ctx.parentWidth
        override fun crossPadding(padding: Spacing) =
            padding.horizontal
        override fun withCross(ctx: MeasureContext, value: Float) =
            ctx.copy(parentWidth = value)
    }
}