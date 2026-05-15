package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaUINode
import computer.obscure.piku.mod.fabric.ui.components.BoxNode
import computer.obscure.piku.mod.fabric.ui.components.ColumnNode
import computer.obscure.piku.mod.fabric.ui.components.GradientNode
import computer.obscure.piku.mod.fabric.ui.components.ProgressBarNode
import computer.obscure.piku.mod.fabric.ui.components.RowNode
import computer.obscure.piku.mod.fabric.ui.components.SpriteNode
import computer.obscure.piku.mod.fabric.ui.components.TextNode
import computer.obscure.piku.mod.fabric.ui.components.UINode
import computer.obscure.twine.annotations.TwineFunction

open class LuaUIContainer(node: UINode) : LuaUINode(node) {

    @TwineFunction
    fun addText(): LuaUIText {
        val child = TextNode("")
        node.children.add(child)
        return LuaUIText(child)
    }

    @TwineFunction
    fun addBox(): LuaUIBox {
        val child = BoxNode()
        node.children.add(child)
        return LuaUIBox(child)
    }

    @TwineFunction
    fun addRow(): LuaUIRow {
        val child = RowNode()
        node.children.add(child)
        return LuaUIRow(child)
    }

    @TwineFunction
    fun addColumn(): LuaUIColumn {
        val child = ColumnNode()
        node.children.add(child)
        return LuaUIColumn(child)
    }

    @TwineFunction
    fun addSprite(path: String): LuaUISprite {
        val child = SpriteNode(path)
        node.children.add(child)
        return LuaUISprite(child)
    }

    @TwineFunction
    fun addGradient(): LuaUIGradient {
        val child = GradientNode()
        node.children.add(child)
        return LuaUIGradient(child)
    }

    @TwineFunction
    fun addProgressBar(): LuaUIProgressBar {
        val child = ProgressBarNode()
        node.children.add(child)
        return LuaUIProgressBar(child)
    }

    @TwineFunction
    fun clear() {
        node.children.clear()
    }
}