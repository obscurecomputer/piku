package computer.obscure.piku.mod.fabric.scripting.api.ui.components

import computer.obscure.piku.mod.fabric.scripting.api.ui.LuaUINode
import computer.obscure.piku.mod.fabric.ui.UIRenderer
import computer.obscure.piku.mod.fabric.ui.components.*
import computer.obscure.twine.annotations.TwineFunction

open class LuaUIContainer(node: UINode) : LuaUINode(node) {

    private fun <T : UINode> attach(child: T): T {
        node.children.add(child)
        UIRenderer.registerNode(child)
        return child
    }

    @TwineFunction
    fun addText(): LuaUIText = LuaUIText(attach(TextNode("")))

    @TwineFunction
    fun addBox(): LuaUIBox = LuaUIBox(attach(BoxNode()))

    @TwineFunction
    fun addRow(): LuaUIRow = LuaUIRow(attach(RowNode()))

    @TwineFunction
    fun addColumn(): LuaUIColumn = LuaUIColumn(attach(ColumnNode()))

    @TwineFunction
    fun addSprite(): LuaUISprite = LuaUISprite(attach(SpriteNode("")))

    @TwineFunction
    fun addGradient(): LuaUIGradient = LuaUIGradient(attach(GradientNode()))

    @TwineFunction
    fun addProgressBar(): LuaUIProgressBar = LuaUIProgressBar(attach(ProgressBarNode()))

    @TwineFunction
    fun addDivider(): LuaUIDivider = LuaUIDivider(attach(DividerNode()))

    @TwineFunction
    fun addScrollbar(): LuaUIScrollbar = LuaUIScrollbar(attach(ScrollbarNode()))

    @TwineFunction
    fun addLine(): LuaUILine = LuaUILine(attach(LineNode()))

    @TwineFunction
    fun clear() {
        node.children.forEach { UIRenderer.deindexTree(it) }
        node.children.clear()
    }
}