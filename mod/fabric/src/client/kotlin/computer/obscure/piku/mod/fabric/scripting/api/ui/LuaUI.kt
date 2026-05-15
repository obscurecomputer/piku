package computer.obscure.piku.mod.fabric.scripting.api.ui

import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIBox
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIColumn
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIRow
import computer.obscure.piku.mod.fabric.ui.UIRenderer
import computer.obscure.piku.mod.fabric.ui.components.BoxNode
import computer.obscure.piku.mod.fabric.ui.components.ColumnNode
import computer.obscure.piku.mod.fabric.ui.components.RowNode
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction

class LuaUI : TwineNative() {

    @TwineFunction
    fun column(): LuaUIColumn {
        val node = ColumnNode()
        UIRenderer.addRoot(node)
        return LuaUIColumn(node)
    }

    @TwineFunction
    fun row(): LuaUIRow {
        val node = RowNode()
        UIRenderer.addRoot(node)
        return LuaUIRow(node)
    }

    @TwineFunction
    fun box(): LuaUIBox {
        val node = BoxNode()
        UIRenderer.addRoot(node)
        return LuaUIBox(node)
    }

    @TwineFunction("get")
    fun getByName(name: String): LuaUINode? {
        return UIRenderer.findByName(name)?.let { LuaUINode.wrap(it) }
    }

    @TwineFunction
    fun getById(id: String): LuaUINode? {
        return UIRenderer.findById(id)?.let { LuaUINode.wrap(it) }
    }

    @TwineFunction
    fun clear() {
        UIRenderer.clearRoots()
    }
}