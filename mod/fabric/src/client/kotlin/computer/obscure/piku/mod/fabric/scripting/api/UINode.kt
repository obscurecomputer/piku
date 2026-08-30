package computer.obscure.piku.mod.fabric.scripting.api

import computer.obscure.piku.mod.fabric.ui.UIRenderer
import computer.obscure.piku.mod.fabric.ui.components.BoxNode
import computer.obscure.piku.mod.fabric.ui.components.ColumnNode
import computer.obscure.piku.mod.fabric.ui.components.UINode

fun ui() = UI

object UI : UIBuilder {
    override fun add(node: UINode): UINode {
        UIRenderer.addRoot(node)
        return node
    }
}

interface UIBuilder {
    fun add(node: UINode): UINode
    fun column(block: ColumnNode.() -> Unit): UINode {
        return add(ColumnNode().apply(block))
    }
    fun box(block: BoxNode.() -> Unit): UINode {
        return add(BoxNode().apply(block))
    }
}