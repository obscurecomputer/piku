package computer.obscure.piku.mod.fabric.scripting.api.ui

import computer.obscure.piku.core.scripting.api.LuaText
import computer.obscure.piku.core.scripting.api.LuaTextInstance
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIBox
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIColumn
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIRow
import computer.obscure.piku.mod.fabric.ui.UIRenderer
import computer.obscure.piku.mod.fabric.ui.components.BoxNode
import computer.obscure.piku.mod.fabric.ui.components.ColumnNode
import computer.obscure.piku.mod.fabric.ui.components.RowNode
import computer.obscure.piku.mod.fabric.ui.menu.UIMenu
import computer.obscure.piku.mod.fabric.utils.toMcComponent
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import net.kyori.adventure.text.Component

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
    fun exists(name: String): Boolean {
        return UIRenderer.findByName(name) != null
    }

    @TwineFunction
    fun existsById(id: String): Boolean {
        return UIRenderer.findById(id) != null
    }

    @TwineFunction
    fun clear() {
        UIRenderer.clearRoots()
    }

    @TwineFunction
    fun menu(title: LuaTextInstance): LuaUIMenuInstance {
        return LuaUIMenuInstance(
            title, UIMenu(title.toMcComponent())
        )
    }

    @TwineFunction
    fun menu(): LuaUIMenuInstance {
        val title = LuaText.fromComponent(Component.empty())
        return LuaUIMenuInstance(
            title, UIMenu(title.toMcComponent())
        )
    }
}