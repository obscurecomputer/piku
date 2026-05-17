package computer.obscure.piku.mod.fabric.scripting.api.ui

import computer.obscure.piku.core.animation.AnimationManager
import computer.obscure.piku.core.classes.Spacing
import computer.obscure.piku.core.scripting.api.LuaColorInstance
import computer.obscure.piku.core.scripting.api.LuaSpacingInstance
import computer.obscure.piku.core.scripting.api.LuaVec2Instance
import computer.obscure.piku.core.scripting.engine.EngineError
import computer.obscure.piku.core.scripting.engine.EngineErrorCode
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIBox
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIColumn
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIDivider
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIGradient
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIProgressBar
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIRow
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIScrollbar
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUISprite
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIText
import computer.obscure.piku.mod.fabric.ui.classes.Anchor
import computer.obscure.piku.mod.fabric.ui.classes.Dimension
import computer.obscure.piku.mod.fabric.ui.UIRenderer
import computer.obscure.piku.mod.fabric.ui.components.BoxNode
import computer.obscure.piku.mod.fabric.ui.components.ColumnNode
import computer.obscure.piku.mod.fabric.ui.components.DividerNode
import computer.obscure.piku.mod.fabric.ui.components.GradientNode
import computer.obscure.piku.mod.fabric.ui.components.ProgressBarNode
import computer.obscure.piku.mod.fabric.ui.components.RowNode
import computer.obscure.piku.mod.fabric.ui.components.ScrollbarNode
import computer.obscure.piku.mod.fabric.ui.components.SpriteNode
import computer.obscure.piku.mod.fabric.ui.components.TextNode
import computer.obscure.piku.mod.fabric.ui.components.UINode
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import computer.obscure.twine.annotations.TwineProperty

open class LuaUINode(open val node: UINode) : TwineNative() {

    @TwineFunction
    fun visible(value: Boolean): LuaUINode {
        node.visible = value
        return this
    }

    @TwineFunction
    fun background(value: LuaColorInstance): LuaUINode {
        node.background = value.toUIColor()
        return this
    }

    @TwineFunction
    fun width(value: String): LuaUINode {
        node.width = parseDimension(value)
        return this
    }

    @TwineFunction
    fun height(value: String): LuaUINode {
        node.height = parseDimension(value)
        return this
    }

    @TwineFunction
    fun size(value: LuaVec2Instance): LuaUINode {
        node.width = Dimension.Fixed(value.x.toFloat())
        node.height = Dimension.Fixed(value.y.toFloat())
        return this
    }

    @TwineFunction
    fun padding(value: LuaSpacingInstance): LuaUINode {
        node.padding = value.toSpacing()
        return this
    }

    @TwineFunction
    fun margin(value: Double): LuaUINode {
        node.margin = Spacing(value)
        return this
    }

    @TwineFunction
    fun anchor(value: String): LuaUINode {
        node.anchor = Anchor.entries.find { it.name.equals(value, ignoreCase = true) }
            ?: throw EngineError(EngineErrorCode.INVALID_ANCHOR, "Unknown anchor \"$value\"")
        return this
    }

    @TwineFunction
    fun offset(value: LuaVec2Instance): LuaUINode {
        node.offset = value.toVec2()
        return this
    }

    @TwineFunction
    fun children(): List<LuaUINode> = node.children.mapNotNull { wrap(it) }

    @TwineFunction
    fun removeChild(child: LuaUINode) {
        node.children.remove(child.node)
    }

    @TwineFunction
    fun contains(x: Float, y: Float): Boolean {
        return x >= node.layoutX && x <= node.layoutX + node.measuredWidth &&
               y >= node.layoutY && y <= node.layoutY + node.measuredHeight
    }

    private fun parseDimension(value: String): Dimension = when {
        value == "wrap" -> Dimension.Wrap
        value == "fill" -> Dimension.Fill
        value.endsWith("%") -> Dimension.Fraction(value.dropLast(1).toFloat() / 100f)
        value.endsWith("px") -> Dimension.Fixed(value.dropLast(2).toFloat())
        else -> Dimension.Fixed(value.toFloat())
    }

    @TwineFunction
    fun name(): String? = node.name

    @TwineFunction
    fun name(value: String): LuaUINode {
        node.name = value
        return this
    }

    @TwineProperty
    val id: String
        get() = node.id

    @TwineFunction
    fun remove() {
        UIRenderer.roots.remove(node)
        // also search and remove from parent if nested
        UIRenderer.roots.forEach { removeFromTree(it, node) }
    }

    private fun removeFromTree(parent: UINode, target: UINode) {
        parent.children.remove(target)
        parent.children.forEach { removeFromTree(it, target) }
    }

    @TwineFunction
    fun exists(name: String): Boolean {
        return node.children.any { searchTree(it, name) != null }
    }

    @TwineFunction("get")
    fun getByName(name: String): LuaUINode? {
        return searchTree(node, name)?.let { wrap(it) }
    }

    private fun searchTree(node: UINode, name: String): UINode? {
        if (node.name == name) return node
        return node.children.firstNotNullOfOrNull { searchTree(it, name) }
    }

    @TwineFunction
    fun animate(): LuaUIAnimation {
        return LuaUIAnimation(this.node)
    }

    @TwineProperty
    val isAnimating: Boolean
        get() = AnimationManager.isAnimating(node.id)

    @TwineFunction
    fun cancelAnimations() {
        AnimationManager.cancelFor(node.id)
    }

    companion object {
        fun wrap(node: UINode): LuaUINode? = when (node) {
            is TextNode -> LuaUIText(node)
            is ColumnNode -> LuaUIColumn(node)
            is RowNode -> LuaUIRow(node)
            is BoxNode -> LuaUIBox(node)
            is ProgressBarNode -> LuaUIProgressBar(node)
            is SpriteNode -> LuaUISprite(node)
            is GradientNode -> LuaUIGradient(node)
            is DividerNode -> LuaUIDivider(node)
            is ScrollbarNode -> LuaUIScrollbar(node)
            else -> null
        }
    }
}