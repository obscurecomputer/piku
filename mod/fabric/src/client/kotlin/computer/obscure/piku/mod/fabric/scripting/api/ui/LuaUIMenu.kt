package computer.obscure.piku.mod.fabric.scripting.api.ui

import computer.obscure.piku.core.scripting.api.LuaTextInstance
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIBox
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIColumn
import computer.obscure.piku.mod.fabric.scripting.api.ui.components.LuaUIRow
import computer.obscure.piku.mod.fabric.ui.components.BoxNode
import computer.obscure.piku.mod.fabric.ui.components.ColumnNode
import computer.obscure.piku.mod.fabric.ui.components.RowNode
import computer.obscure.piku.mod.fabric.ui.menu.UIMenu
import computer.obscure.twine.TwineNative
import computer.obscure.twine.annotations.TwineFunction
import net.minecraft.client.Minecraft

class LuaUIMenuInstance(
    val title: LuaTextInstance,
    val screen: UIMenu,
) : TwineNative() {

    @TwineFunction
    fun escapeClose(value: Boolean) = apply {
        screen.escapeClose = value
    }

    @TwineFunction
    fun blur(value: Boolean) = apply {
        screen.blur = value
    }

    @TwineFunction
    fun column(): LuaUIColumn {
        val node = ColumnNode()
        screen.addRoot(node)
        return LuaUIColumn(node)
    }

    @TwineFunction
    fun row(): LuaUIRow {
        val node = RowNode()
        screen.addRoot(node)
        return LuaUIRow(node)
    }

    @TwineFunction
    fun box(): LuaUIBox {
        val node = BoxNode()
        screen.addRoot(node)
        return LuaUIBox(node)
    }

    @TwineFunction
    fun open() {
        Minecraft.getInstance().gui.setScreen(screen)
    }
}