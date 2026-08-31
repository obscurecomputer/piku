package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.piku.mod.fabric.ui.components.BoxNode
import computer.obscure.piku.mod.fabric.ui.components.ColumnNode
import computer.obscure.piku.mod.fabric.ui.components.DividerNode
import computer.obscure.piku.mod.fabric.ui.components.GradientNode
import computer.obscure.piku.mod.fabric.ui.components.LineNode
import computer.obscure.piku.mod.fabric.ui.components.ProgressBarNode
import computer.obscure.piku.mod.fabric.ui.components.RowNode
import computer.obscure.piku.mod.fabric.ui.components.ScrollbarNode
import computer.obscure.piku.mod.fabric.ui.components.SpriteNode
import computer.obscure.piku.mod.fabric.ui.components.TextNode
import computer.obscure.piku.mod.fabric.ui.components.UINode

interface UIBuilder {
    fun getByName(name: String): UINode?
    fun getById(id: String): UINode?
    fun exists(name: String) = getByName(name) != null
    fun existsById(id: String) = getById(id) != null

    fun searchTree(node: UINode, name: String): UINode? {
        if (node.name == name) return node
        return node.children.firstNotNullOfOrNull {
            searchTree(it, name)
        }
    }

    fun <T : UINode> add(node: T): T
    fun column(block: ColumnNode.() -> Unit): ColumnNode {
        return add(ColumnNode().apply(block))
    }
    fun row(block: RowNode.() -> Unit): RowNode {
        return add(RowNode().apply(block))
    }
    fun box(block: BoxNode.() -> Unit): BoxNode {
        return add(BoxNode().apply(block))
    }
    fun text(block: TextNode.() -> Unit): TextNode {
        return add(TextNode().apply(block))
    }
    fun textInput(block: BoxNode.() -> Unit): BoxNode {
        return add(BoxNode().apply(block))
    }
    fun sprite(block: SpriteNode.() -> Unit): SpriteNode {
        return add(SpriteNode().apply(block))
    }
    fun gradient(block: GradientNode.() -> Unit): GradientNode {
        return add(GradientNode().apply(block))
    }
    fun progressBar(block: ProgressBarNode.() -> Unit): ProgressBarNode {
        return add(ProgressBarNode().apply(block))
    }
    fun divider(block: DividerNode.() -> Unit): DividerNode {
        return add(DividerNode().apply(block))
    }
    fun scrollbar(block: ScrollbarNode.() -> Unit): ScrollbarNode {
        return add(ScrollbarNode().apply(block))
    }
    fun line(block: LineNode.() -> Unit): LineNode {
        return add(LineNode().apply(block))
    }
}